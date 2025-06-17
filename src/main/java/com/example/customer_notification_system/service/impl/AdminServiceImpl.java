package com.example.customer_notification_system.service.impl;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.entity.*;
import com.example.customer_notification_system.repository.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.exception.ResourceNotFoundException; // Import custom exception
import com.example.customer_notification_system.exception.DuplicateResourceException; // Import custom exception
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder; // New import

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO createAdmin(String username, String password) {
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Admin with username '" + username + "' already exists.");
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password)); // Encode password
        admin.setRole("ROLE_ADMIN"); // Assign a default role
        Admin saved = adminRepository.save(admin);
        return new AdminDTO(saved.getId(), saved.getUsername());
    }

    @Override
    public AdminDTO getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Admin not found with username: " + username) // Use specific exception
        );
        return new AdminDTO(admin.getId(), admin.getUsername());
    }