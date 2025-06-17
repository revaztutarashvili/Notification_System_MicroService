package com.example.customer_notification_system.dto.requests;

import com.example.customer_notification_system.enums.NotificationChannel;
import com.example.customer_notification_system.enums.NotificationStatusType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackNotificationRequest {
    private Long customerId;
    private NotificationChannel channel;
    private NotificationStatusType status;
    private String messageId;
}
