package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.security.CustomUserDetails; // Import CustomUserDetails
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import @PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import @AuthenticationPrincipal
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {
    private final NotificationPreferenceService preferenceService;

    /**
     * Retrieves the notification preferences for a specific customer.
     * Accessible by ADMINs (for any customer) or by USERs (only for their own preferences).
     *
     * @param customerId The ID of the customer whose preferences are to be retrieved.
     * @param currentUser The authenticated user's details.
     * @return ResponseEntity with the NotificationPreferenceDTO and HTTP status 200 OK.
     */
    @GetMapping("/customer/{customerId}") // GET /api/preferences/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #customerId == authentication.principal.id)")
    public ResponseEntity<NotificationPreferenceDTO> getPreferences(@PathVariable Long customerId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(preferenceService.getPreferencesByCustomerId(customerId));
    }

    /**
     * Updates the notification preferences for a specific customer.
     * Accessible by ADMINs (for any customer) or by USERs (only for their own preferences).
     *
     * @param customerId The ID of the customer whose preferences are being updated.
     * @param request The DTO containing the updated notification preference details.
     * @param currentUser The authenticated user's details.
     * @return ResponseEntity with the updated NotificationPreferenceDTO and HTTP status 200 OK.
     */
    @PutMapping("/customer/{customerId}") // PUT /api/preferences/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #customerId == authentication.principal.id)")
    public ResponseEntity<NotificationPreferenceDTO> updatePreferences(@PathVariable Long customerId, @RequestBody UpdateNotificationPreferenceRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(preferenceService.updatePreferences(customerId, request));
    }

    // TODO: Consider adding POST/DELETE if preferences can be explicitly created/deleted
}
