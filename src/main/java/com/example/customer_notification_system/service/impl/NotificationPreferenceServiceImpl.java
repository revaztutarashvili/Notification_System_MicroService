package com.example.customer_notification_system.service.impl;

import com.example.customer_notification_system.dto.NotificationPreferenceDTO;
import com.example.customer_notification_system.dto.requests.UpdateNotificationPreferenceRequest;
import com.example.customer_notification_system.entity.Customer;
import com.example.customer_notification_system.entity.NotificationPreference;
import com.example.customer_notification_system.exception.ResourceNotFoundException;
import com.example.customer_notification_system.mapper.EntityMapper;
import com.example.customer_notification_system.repository.CustomerRepository;
import com.example.customer_notification_system.repository.NotificationPreferenceRepository;
import com.example.customer_notification_system.service.NotificationPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {
    private final NotificationPreferenceRepository preferenceRepository;
    private final CustomerRepository customerRepository; // Needed to find customer

    @Override
    public NotificationPreferenceDTO getPreferencesByCustomerId(Long customerId) {
        return preferenceRepository.findByCustomerId(customerId)
                .map(EntityMapper::toPreferenceDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Notification preferences not found for customer ID: " + customerId));
    }

    @Override
    public NotificationPreferenceDTO updatePreferences(Long customerId, UpdateNotificationPreferenceRequest request) {
        // Find existing preferences or create new ones if they don't exist (idempotent PUT)
        NotificationPreference preference = preferenceRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    // If preferences don't exist, create a new one
                    Customer customer = customerRepository.findById(customerId)
                            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
                    NotificationPreference newPreference = new NotificationPreference();
                    newPreference.setCustomer(customer);
                    return newPreference;
                });

        preference.setEmailOptIn(request.isEmailOptIn());
        preference.setSmsOptIn(request.isSmsOptIn());
        preference.setPromoOptIn(request.isPromoOptIn());

        return EntityMapper.toPreferenceDTO(preferenceRepository.save(preference));
    }

    // TODO: Consider adding a create method if initial preferences are managed separately from update.
    // Example:
    /*
    public NotificationPreferenceDTO createPreferences(Long customerId, CreateNotificationPreferenceRequest request) {
        if (preferenceRepository.findByCustomerId(customerId).isPresent()) {
            throw new DuplicateResourceException("Notification preferences already exist for customer ID: " + customerId);
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));

        NotificationPreference newPreference = new NotificationPreference();
        newPreference.setCustomer(customer);
        newPreference.setEmailOptIn(request.isEmailOptIn());
        newPreference.setSmsOptIn(request.isSmsOptIn());
        newPreference.setPromoOptIn(request.isPromoOptIn());
        return EntityMapper.toPreferenceDTO(preferenceRepository.save(newPreference));
    }
    */
}
