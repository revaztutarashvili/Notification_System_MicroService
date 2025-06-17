package com.example.customer_notification_system.service;
import com.example.customer_notification_system.dto.*;

public interface AdminService {
    AdminDTO createAdmin(String username, String password, String role); // Updated signature
    AdminDTO getAdminByUsername(String username);

    // TODO: Add methods for updating and deleting admins if required by the task
    // AdminDTO updateAdmin(Long id, UpdateAdminRequest request); // Example update
    // void deleteAdmin(Long id); // Example delete
}
