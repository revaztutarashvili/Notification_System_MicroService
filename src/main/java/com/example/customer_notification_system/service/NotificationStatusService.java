package com.example.customer_notification_system.service;
import java.util.List;
import com.example.customer_notification_system.dto.NotificationStatusDTO;

public interface NotificationStatusService {
    NotificationStatusDTO trackNotification(Long customerId, String channel, String status, String messageId);
    List<NotificationStatusDTO> getStatusByCustomerId(Long customerId);
    List<NotificationStatusDTO> getStatusByStatus(String status);
}
