package com.example.customer_notification_system.config;

import com.example.customer_notification_system.repository.CustomerRepository;
import com.example.customer_notification_system.repository.AdminRepository;
import com.example.customer_notification_system.security.JwtAuthenticationFilter;
import com.example.customer_notification_system.security.JwtTokenProvider;
import com.example.customer_notification_system.security.CustomUserDetails;
import com.example.customer_notification_system.entity.Admin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // For @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize and @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible endpoints (no authentication required)
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/customers/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        // Initial SuperAdmin Creation (only if no admins exist)
                        .requestMatchers("/api/admins/initial-superadmin").permitAll() // Temporarily permit for initial setup
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            // First, try to find the user as an Admin
            return adminRepository.findByUsername(username)
                    .map(admin -> {
                        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(admin.getRole()));
                        return new CustomUserDetails(admin.getId(), admin.getUsername(), admin.getPassword(), authorities, admin.getRole());
                    })
                    .orElseGet(() -> {
                        // If not found as Admin, try to find as a Customer
                        return customerRepository.findByUsername(username)
                                .map(customer -> {
                                    List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                                    return new CustomUserDetails(customer.getId(), customer.getUsername(), customer.getPassword(), authorities, "ROLE_USER");
                                })
                                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
                    });
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Bean to create initial SuperAdmin on startup if no admins exist
    @Bean
    public OncePerRequestFilter initialSuperAdminCreator(PasswordEncoder passwordEncoder) {
        return new OncePerRequestFilter() {
            private boolean superAdminCreationAttempted = false; // Flag to ensure creation happens only once per app lifetime

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                // Ensure this logic runs only once per application startup
                if (!superAdminCreationAttempted) {
                    if (adminRepository.count() == 0) { // Check if there are no admins in the DB
                        // Create SuperAdmin only if no admins exist in the DB
                        Admin superAdmin = new Admin();
                        superAdmin.setUsername("superadmin");
                        superAdmin.setPassword(passwordEncoder.encode("123")); // Hashed password for superadmin
                        superAdmin.setRole("ROLE_SUPER_ADMIN");
                        adminRepository.save(superAdmin);
                        System.out.println("SuperAdmin 'superadmin' created with password '123'.");
                    }
                    superAdminCreationAttempted = true; // Mark as attempted
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
