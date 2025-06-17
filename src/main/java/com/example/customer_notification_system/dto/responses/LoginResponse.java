package com.example.customer_notification_system.dto.responses; // Changed to responses package

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String jwtToken;
    private Long id;
    private String username;
    private String role; // Role of the authenticated user
}
