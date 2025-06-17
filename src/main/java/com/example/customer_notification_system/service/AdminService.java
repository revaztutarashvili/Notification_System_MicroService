package com.example.customer_notification_system.service;
import com.example.customer_notification_system.dto.*;

public interface AdminService {
    AdminDTO createAdmin(String username, String password);
    AdminDTO getAdminByUsername(String username);
}
