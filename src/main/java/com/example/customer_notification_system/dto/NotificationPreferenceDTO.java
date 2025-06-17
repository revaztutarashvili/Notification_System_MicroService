package com.example.customer_notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPreferenceDTO {
    private Long id;
    private Long customerId;
    private boolean smsOptIn;
    private boolean emailOptIn;
    private boolean promoOptIn;
}
