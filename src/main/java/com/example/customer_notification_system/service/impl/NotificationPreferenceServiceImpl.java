package com.example.customer_notification_system.service.impl;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.UpdateNotificationPreferenceRequest;
import com.example.customer_notification_system.entity.*;
import com.example.customer_notification_system.repository.*;
import com.example.customer_notification_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {
    private final NotificationPreferenceRepository preferenceRepository;
    private final CustomerRepository customerRepository;

    @Override
    public NotificationPreferenceDTO getPreferencesByCustomerId(Long customerId) {
        return preferenceRepository.findByCustomerId(customerId)
                .map(this::toDTO).orElseThrow();
    }

    @Override
    public NotificationPreferenceDTO updatePreferences(UpdateNotificationPreferenceRequest request) {
        NotificationPreference preference = preferenceRepository.findByCustomerId(request.getCustomerId()).orElseThrow();
        preference.setEmailOptIn(request.isEmailOptIn());
        preference.setSmsOptIn(request.isSmsOptIn());
        preference.setPromoOptIn(request.isPromoOptIn());
        return toDTO(preferenceRepository.save(preference));
    }

    private NotificationPreferenceDTO toDTO(NotificationPreference preference) {
        return new NotificationPreferenceDTO(
                preference.getId(),
                preference.getCustomer().getId(),
                preference.isEmailOptIn(),
                preference.isSmsOptIn(),
                preference.isPromoOptIn()
        );
    }
}