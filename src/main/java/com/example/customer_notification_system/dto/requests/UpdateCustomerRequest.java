package com.example.customer_notification_system.dto.requests;

import com.example.customer_notification_system.enums.NotificationChannel;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateCustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Set<NotificationChannel> notificationPreferences;
}
