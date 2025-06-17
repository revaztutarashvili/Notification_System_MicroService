package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationStatusController {
    private final NotificationStatusService notificationStatusService;

    @PostMapping("/track")
    public ResponseEntity<NotificationStatusDTO> trackNotification(@RequestBody TrackNotificationRequest request) {
        return ResponseEntity.ok(notificationStatusService.trackNotification(
                request.getCustomerId(),
                request.getChannel().name(),
                request.getStatus().name(),
                request.getMessageId()));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationStatusDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(notificationStatusService.getStatusByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationStatusDTO>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(notificationStatusService.getStatusByStatus(status));
    }
}