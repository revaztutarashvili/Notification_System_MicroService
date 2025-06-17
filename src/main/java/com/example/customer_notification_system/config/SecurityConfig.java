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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // Will be less relevant for web UI, but keep for API
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
    private final JwtTokenProvider jwtTokenProvider; // Still used for API endpoints

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configure CSRF (Cross-Site Request Forgery) protection
                // For development and simple HTML forms, we might disable it for now.
                // For production web UIs, enable it and use Thymeleaf's _csrf token in forms.
                .csrf(csrf -> csrf.disable()) // Temporarily disable CSRF for simpler HTML forms

                // Configure CORS (Cross-Origin Resource Sharing)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Define authorization rules for HTTP requests
                .authorizeHttpRequests(authorize -> authorize
                        // Publicly accessible API endpoints (no authentication required, e.g., for mobile/external apps)
                        .requestMatchers("/api/auth/login").permitAll()
                        .requestMatchers("/api/customers/register").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll() // Allow Swagger UI access

                        // Initial SuperAdmin Creation (only if no admins exist) - this path will only work once on startup
                        .requestMatchers("/initial-superadmin-setup").permitAll() // Web path for initial setup status (optional)

                        // Publicly accessible Web UI pages
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // Allow login, register pages and static resources
                        .requestMatchers("/").permitAll() // Allow root path to redirect (handled by controller)

                        // Admin-only Web UI pages
                        .requestMatchers("/admin/**").hasAnyRole("SUPER_ADMIN", "ADMIN") // Admin dashboard and management pages
                        .requestMatchers("/superadmin/**").hasRole("SUPER_ADMIN") // SuperAdmin specific pages (if any)

                        // User-only Web UI pages
                        .requestMatchers("/user/**").hasRole("USER") // User dashboard and profile pages

                        // All other API endpoints (prefixed with /api/) require authentication.
                        // NOTE: JWT filter ensures API endpoints still require token even if web paths allow access
                        .requestMatchers("/api/**").authenticated()

                        // All other unmatched requests require authentication (default catch-all for web)
                        .anyRequest().authenticated()
                )
                // Configure Form-based Login for Web UI
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page URL
                        .defaultSuccessUrl("/dashboard", true) // Redirect after successful login (true forces redirection even if already logged in)
                        .failureUrl("/login?error=true") // Redirect on failed login
                        .permitAll() // Allow everyone to access the login page
                )
                // Configure Logout for Web UI
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL to trigger logout
                        .logoutSuccessUrl("/login?logout=true") // Redirect after successful logout
                        .invalidateHttpSession(true) // Invalidate HTTP session
                        .deleteCookies("JSESSIONID") // Delete session cookie
                        .permitAll() // Allow everyone to access the logout URL
                )
                // Disable HTTP Basic authentication (no longer needed for web UI after form login)
                .httpBasic(httpBasic -> httpBasic.disable())
                // JWT filter for API endpoints (stateless) - keep for API access
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)); // Use IF_REQUIRED for session-based login, STATELESS for API if needed.

        // Add JWT filter only if the request is for an API endpoint
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService()), UsernamePasswordAuthenticationFilter.class);


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
        // Allow common origins for development (e.g., your React app, if you ever use it for APIs)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Bean to create initial SuperAdmin on startup if no admins exist
    // This filter will be executed for every request, but the logic inside runs only once.
    @Bean
    public OncePerRequestFilter initialSuperAdminCreator(PasswordEncoder passwordEncoder) {
        return new OncePerRequestFilter() {
            private boolean superAdminCreationAttempted = false; // Flag to ensure creation happens only once

            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
                if (!superAdminCreationAttempted) {
                    if (adminRepository.count() == 0) { // Check if no admins exist in the DB
                        Admin superAdmin = new Admin();
                        superAdmin.setUsername("superadmin");
                        superAdmin.setPassword(passwordEncoder.encode("123")); // Hashed password for superadmin
                        superAdmin.setRole("ROLE_SUPER_ADMIN");
                        adminRepository.save(superAdmin);
                        superAdminCreationAttempted = true;
                        System.out.println("SuperAdmin 'superadmin' created with password '123'.");
                    } else {
                        superAdminCreationAttempted = true; // Mark as attempted if admins already exist
                    }
                }
                filterChain.doFilter(request, response);
            }
        };
    }
}
