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


    @PostMapping("/track") // POST /api/notifications/track
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<NotificationStatusDTO> trackNotification(@RequestBody TrackNotificationRequest request) {
        return ResponseEntity.ok(notificationStatusService.trackNotification(
                request.getCustomerId(),
                request.getChannel().name(),
                request.getStatus().name(),
                request.getMessageId()));
    }


    @GetMapping("/customer/{customerId}") // GET /api/notifications/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can view customer notification statuses
    public ResponseEntity<List<NotificationStatusDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(notificationStatusService.getStatusByCustomerId(customerId));
    }


    @GetMapping("/status/{status}") // GET /api/notifications/status/{status}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<NotificationStatusDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationStatusService.getStatusByStatus(status));
    }

}