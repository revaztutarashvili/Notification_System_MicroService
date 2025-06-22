package com.example.customer_notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String username; // Username field to display
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<AddressDTO> addresses; // Assuming addresses are part of the customer DTO representation
    private NotificationPreferenceDTO notificationPreference; // Assuming notification preferences are part of the customer DTO representation
}
