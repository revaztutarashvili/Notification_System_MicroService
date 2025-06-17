package com.example.customer_notification_system.service.impl;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.entity.*;
import com.example.customer_notification_system.repository.*;
import com.example.customer_notification_system.service.*;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public AdminDTO createAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(password); // Password should be encoded
        Admin saved = adminRepository.save(admin);
        return new AdminDTO(saved.getId(), saved.getUsername());
    }

    @Override
    public AdminDTO getAdminByUsername(String username) {
        Admin admin = adminRepository.findByUsername(username).orElseThrow();
        return new AdminDTO(admin.getId(), admin.getUsername());
    }
}