package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {
    private final NotificationPreferenceService preferenceService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<NotificationPreferenceDTO> getPreferences(@PathVariable Long customerId) {
        return ResponseEntity.ok(preferenceService.getPreferencesByCustomerId(customerId));
    }

    @PutMapping
    public ResponseEntity<NotificationPreferenceDTO> updatePreferences(@RequestBody UpdateNotificationPreferenceRequest request) {
        return ResponseEntity.ok(preferenceService.updatePreferences(request));
    }
}