package com.example.customer_notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor // <-- დაამატეთ ეს ანოტაცია
@NoArgsConstructor
@Data
public class AdminDTO {
    private Long id;
    private String username;
}
