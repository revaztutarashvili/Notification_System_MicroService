package com.example.customer_notification_system.service.impl;

import com.example.customer_notification_system.dto.AdminDTO;
import com.example.customer_notification_system.dto.requests.UpdateAdminRequest;
import com.example.customer_notification_system.entity.Admin;
import com.example.customer_notification_system.exception.DuplicateResourceException;
import com.example.customer_notification_system.exception.ResourceNotFoundException;
import com.example.customer_notification_system.repository.AdminRepository;
import com.example.customer_notification_system.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    @Override
    @Transactional
    public AdminDTO createAdmin(String username, String password, String role) {
        if (adminRepository.findByUsername(username).isPresent()) {
            throw new DuplicateResourceException("Admin with username " + username + " already exists.");
        }
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password)); // Encode password
        admin.setRole(role);
        return toDTO(adminRepository.save(admin));
    }

    @Override
    public AdminDTO getAdminByUsername(String username) {
        return adminRepository.findByUsername(username)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with username: " + username));
    }

    @Override
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDTO getAdminById(Long id) {
        return adminRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));
    }

    @Override
    @Transactional
    public AdminDTO updateAdmin(Long id, UpdateAdminRequest request) {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Admin not found with id: " + id));

        // Check if username is being changed and if it already exists for another admin
        if (!admin.getUsername().equals(request.getUsername()) &&
                adminRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }

        admin.setUsername(request.getUsername());
        admin.setRole(request.getRole()); // Update role

        // Password update would need a separate mechanism
        return toDTO(adminRepository.save(admin));
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    private AdminDTO toDTO(Admin admin) {
        return new AdminDTO(admin.getId(), admin.getUsername(), admin.getRole());
    }
}