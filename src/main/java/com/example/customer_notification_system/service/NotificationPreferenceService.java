package com.example.customer_notification_system.service;

import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.UpdateNotificationPreferenceRequest;


public interface NotificationPreferenceService {
    NotificationPreferenceDTO getPreferencesByCustomerId(Long customerId);
    NotificationPreferenceDTO updatePreferences(UpdateNotificationPreferenceRequest request);
}
