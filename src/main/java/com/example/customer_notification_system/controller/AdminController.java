package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<AdminDTO> createAdmin(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(adminService.createAdmin(username, password));
    }

    @GetMapping("/{username}")
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable String username) {
        return ResponseEntity.ok(adminService.getAdminByUsername(username));
    }
}