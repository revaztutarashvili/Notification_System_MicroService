package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.AdminDTO;
import com.example.customer_notification_system.dto.requests.CreateAdminRequest;
import com.example.customer_notification_system.dto.requests.UpdateAdminRequest;
import com.example.customer_notification_system.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    // Existing or planned: Create Admin
    @PostMapping
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<AdminDTO> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
        AdminDTO createdAdmin = adminService.createAdmin(request.getUsername(), request.getPassword(), request.getRole());
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    // Get Admin by Username
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<AdminDTO> getAdminByUsername(@PathVariable String username) {
        return ResponseEntity.ok(adminService.getAdminByUsername(username));
    }

    // New: Update Admin
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable Long id, @Valid @RequestBody UpdateAdminRequest request) {
        AdminDTO updatedAdmin = adminService.updateAdmin(id, request);
        return ResponseEntity.ok(updatedAdmin);
    }

    // New: Delete Admin
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }


}