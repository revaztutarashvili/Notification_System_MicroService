package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences") // Base path for notification preference operations
@RequiredArgsConstructor
public class NotificationPreferenceController {
    private final NotificationPreferenceService preferenceService;

    /**
     * Retrieves the notification preferences for a specific customer.
     *
     * @param customerId The ID of the customer whose preferences are to be retrieved.
     * @return ResponseEntity with the NotificationPreferenceDTO and HTTP status 200 OK.
     */
    @GetMapping("/customer/{customerId}") // GET /api/preferences/customer/{customerId}
    public ResponseEntity<NotificationPreferenceDTO> getPreferences(@PathVariable Long customerId) {
        return ResponseEntity.ok(preferenceService.getPreferencesByCustomerId(customerId));
    }

    /**
     * Updates the notification preferences for a specific customer.
     * The customer ID is part of the path variable for a more RESTful approach.
     *
     * @param customerId The ID of the customer whose preferences are being updated.
     * @param request The DTO containing the updated notification preference details.
     * @return ResponseEntity with the updated NotificationPreferenceDTO and HTTP status 200 OK.
     */
    @PutMapping("/customer/{customerId}") // PUT /api/preferences/customer/{customerId}
    public ResponseEntity<NotificationPreferenceDTO> updatePreferences(@PathVariable Long customerId, @RequestBody UpdateNotificationPreferenceRequest request) {
        // Ensure your NotificationPreferenceService.updatePreferences signature is updated to accept customerId
        return ResponseEntity.ok(preferenceService.updatePreferences(customerId, request));
    }

    // TODO: Consider adding a POST endpoint if initial preferences are not created automatically

}
