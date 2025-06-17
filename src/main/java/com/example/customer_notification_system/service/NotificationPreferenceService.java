package com.example.customer_notification_system.service;

import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.UpdateNotificationPreferenceRequest;


public interface NotificationPreferenceService {
    NotificationPreferenceDTO getPreferencesByCustomerId(Long customerId);
    // Updated signature to include customerId from the path
    NotificationPreferenceDTO updatePreferences(Long customerId, UpdateNotificationPreferenceRequest request);

    // TODO: Consider adding a createPreferences method if initial preferences are not created automatically
    // or if PUT doesn't handle creation idempotently in your implementation.
    // Example: NotificationPreferenceDTO createPreferences(CreateNotificationPreferenceRequest request);
}
