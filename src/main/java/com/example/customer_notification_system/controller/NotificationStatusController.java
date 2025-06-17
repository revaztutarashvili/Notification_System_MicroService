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
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationStatusController {
    private final NotificationStatusService notificationStatusService;

    /**
     * Tracks the status of a sent notification. Only accessible by ADMINs.
     */
    @PostMapping("/track") // POST /api/notifications/track
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationStatusDTO> trackNotification(@RequestBody TrackNotificationRequest request) {
        return ResponseEntity.ok(notificationStatusService.trackNotification(
                request.getCustomerId(),
                request.getChannel().name(),
                request.getStatus().name(),
                request.getMessageId()));
    }

    /**
     * Retrieves a list of all notification statuses for a specific customer.
     * Accessible by ADMINs (for any customer) or by USERs (only for their own statuses).
     */
    @GetMapping("/customer/{customerId}") // GET /api/notifications/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #customerId == authentication.principal.id)")
    public ResponseEntity<List<NotificationStatusDTO>> getByCustomer(@PathVariable Long customerId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(notificationStatusService.getStatusByCustomerId(customerId));
    }

    /**
     * Retrieves a list of notification statuses filtered by a specific status type. Only accessible by ADMINs.
     */
    @GetMapping("/status/{status}") // GET /api/notifications/status/{status}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<NotificationStatusDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationStatusService.getStatusByStatus(status));
    }

    // TODO: Consider adding endpoints for reporting on notification delivery success rates, etc. (ADMIN only)
}
