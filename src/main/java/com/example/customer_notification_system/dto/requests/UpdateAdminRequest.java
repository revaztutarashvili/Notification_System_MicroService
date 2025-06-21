package com.example.customer_notification_system.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminRequest {
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    private String role; // e.g., "ROLE_ADMIN", "ROLE_SUPER_ADMIN"
}