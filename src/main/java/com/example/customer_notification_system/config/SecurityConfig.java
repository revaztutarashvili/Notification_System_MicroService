package com.example.customer_notification_system.config; // Adjust package as needed

import com.example.customer_notification_system.repository.CustomerRepository; // Import your CustomerRepository
import com.example.customer_notification_system.repository.AdminRepository; // Import AdminRepository if you want to also manage Admin users for login later
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Recommended password encoder
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // For injecting repositories
public class SecurityConfig {

    private final CustomerRepository customerRepository; // For loading registered customers
    private final AdminRepository adminRepository; // For loading admin users

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for simpler API usage (consider enabling for web UIs with CSRF tokens)
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible endpoints
                        .requestMatchers("/api/customers/register").permitAll() // Allow unauthenticated access to registration
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll() // Allow Swagger UI access
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .httpBasic(org.springframework.security.config.Customizer.withDefaults()) // Enable HTTP Basic Auth
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Use stateless sessions for REST APIs
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for strong password hashing
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // This UserDetailsService will attempt to load users first as an Admin, then as a Customer.
        // You might want a more sophisticated role-based UserDetailsService in a real app.
        return username -> {
            // Try to find as Admin first
            return adminRepository.findByUsername(username)
                    .map(admin -> org.springframework.security.core.userdetails.User
                            .withUsername(admin.getUsername())
                            .password(admin.getPassword()) // Hashed password from DB
                            .roles(admin.getRole() != null ? admin.getRole().replace("ROLE_", "") : "ADMIN") // Use role from Admin entity
                            .build())
                    .orElseGet(() -> {
                        // If not found as Admin, try to find as Customer
                        return customerRepository.findByUsername(username)
                                .map(customer -> org.springframework.security.core.userdetails.User
                                        .withUsername(customer.getUsername())
                                        .password(customer.getPassword()) // Hashed password from DB
                                        .roles("USER") // Assign a default role for registered customers
                                        .build())
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
                    });
        };
    }
}
