package com.example.customer_notification_system.service.impl;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.entity.*;
import com.example.customer_notification_system.repository.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.exception.ResourceNotFoundException;
import com.example.customer_notification_system.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AdminDTO createAdmin(String username, String password, String role) { // Updated signature
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Admin with username '" + username + "' already exists.");
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password)); // Encode password
        admin.setRole(role); // Set the provided role
        Admin saved = adminRepository.save(admin);
        return new AdminDTO(saved.getId(), saved.getUsername());
    }

    @Override
    public AdminDTO getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Admin not found with username: " + username)
        );
        return new AdminDTO(admin.getId(), admin.getUsername());
    }

    // TODO: Add updateAdmin and deleteAdmin methods as per task requirements
    // Example:
    /*
    @Override
    public AdminDTO updateAdmin(Long id, UpdateAdminRequest request) { // You need to define UpdateAdminRequest DTO
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with ID: " + id));
        if (request.getUsername() != null && !admin.getUsername().equals(request.getUsername())) {
             if (adminRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
            }
            admin.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // Update other fields as needed, including role if allowed
        return new AdminDTO(adminRepository.save(admin).getId(), admin.getUsername());
    }

    @Override
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found with ID: " + id);
        }
        adminRepository.deleteById(id);
    }
    */
}
