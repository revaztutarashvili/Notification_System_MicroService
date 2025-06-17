package com.example.customer_notification_system.dto.requests;

import lombok.Data;

@Data
public class UpdateNotificationPreferenceRequest {
    private Long customerId;
    private boolean smsOptIn;
    private boolean emailOptIn;
    private boolean promoOptIn;
}
