package com.example.customer_notification_system.service;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.UpdateAdminRequest; // Make sure this is imported

import java.util.List;

public interface AdminService {
    AdminDTO createAdmin(String username, String password, String role);
    AdminDTO getAdminByUsername(String username);
    List<AdminDTO> getAllAdmins(); // Added for AdminWebController
    AdminDTO getAdminById(Long id); // Added for AdminWebController
    AdminDTO updateAdmin(Long id, UpdateAdminRequest request); // Corrected signature
    void deleteAdmin(Long id); // Added for delete functionality
}