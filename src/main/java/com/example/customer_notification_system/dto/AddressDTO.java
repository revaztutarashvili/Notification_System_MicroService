package com.example.customer_notification_system.dto.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data // Use Data for convenience
public class UpdateCustomerRequest {
    private String fullName;    // Changed from firstName/lastName
    private String email;
    private String phoneNumber;
    // Removed 'notificationPreferences' as it's managed via NotificationPreferenceController
}
