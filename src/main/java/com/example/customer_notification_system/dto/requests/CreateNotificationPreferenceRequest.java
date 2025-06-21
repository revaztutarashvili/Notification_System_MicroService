package com.example.customer_notification_system.dto.requests;

import com.example.customer_notification_system.enums.NotificationChannel;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationPreferenceRequest {
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Notification channel cannot be null")
    private NotificationChannel channel;

    @NotNull(message = "Enabled status cannot be null")
    private Boolean enabled;
}