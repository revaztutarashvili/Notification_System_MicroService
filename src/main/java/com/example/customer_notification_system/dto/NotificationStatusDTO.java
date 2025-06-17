package com.example.customer_notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotificationStatusDTO {
    private Long id;
    private String channel;
    private String status;
    private String messageId;
    private LocalDateTime timestamp;
}
