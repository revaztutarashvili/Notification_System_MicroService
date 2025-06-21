package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.requests.LoginRequest;
import com.example.customer_notification_system.dto.responses.LoginResponse; // Make sure this import is correct
import com.example.customer_notification_system.entity.Admin;
import com.example.customer_notification_system.entity.Customer;
import com.example.customer_notification_system.repository.AdminRepository; // Import AdminRepository
import com.example.customer_notification_system.repository.CustomerRepository; // Import CustomerRepository
import com.example.customer_notification_system.security.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AdminRepository adminRepository; // Inject AdminRepository
    private final CustomerRepository customerRepository; // Inject CustomerRepository

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        // Determine user role and ID to return in the response
        String role = null; // Default to null/empty if no explicit role
        Long userId = null;

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().contains("ADMIN"))) { // Check for admin roles
            Optional<Admin> admin = adminRepository.findByUsername(loginRequest.getUsername());
            if (admin.isPresent()) {
                userId = admin.get().getId();
                role = admin.get().getRole(); // Use the specific role from Admin entity (e.g., ROLE_SUPER_ADMIN)
            }
        } else { // It's a customer, but they don't have ROLE_USER anymore
            Optional<Customer> customer = customerRepository.findByUsername(loginRequest.getUsername());
            if (customer.isPresent()) {
                userId = customer.get().getId();
                role = ""; // Explicitly set to empty string if no role for non-admin users
            }
        }

        return ResponseEntity.ok(new LoginResponse(jwt, userId, loginRequest.getUsername(), role));
    }
}