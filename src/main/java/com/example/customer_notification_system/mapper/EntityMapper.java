package com.example.customer_notification_system.mapper;
import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime; // Required for NotificationStatusDTO mapping
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.entity.*;


public class EntityMapper {

    public static CustomerDTO toCustomerDTO(Customer customer) {
        List<AddressDTO> addressDTOs = customer.getAddresses() != null
                ? customer.getAddresses().stream()
                .map(EntityMapper::toAddressDTO)
                .collect(Collectors.toList())
                : null;

        NotificationPreferenceDTO preferenceDTO = customer.getNotificationPreference() != null
                ? toPreferenceDTO(customer.getNotificationPreference())
                : null;

        return new CustomerDTO(
                customer.getId(),
                customer.getUsername(), // New: Map username from entity to DTO
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                addressDTOs,
                preferenceDTO
        );
    }

    public static AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;

        // Ensure this matches the AddressDTO constructor: AddressDTO(Long id, String type, String value)
        return new AddressDTO(
                address.getId(),
                address.getType() != null ? address.getType().name() : null, // enum to String
                address.getValue()
        );
    }

    public static NotificationPreferenceDTO toPreferenceDTO(NotificationPreference preference) {
        return new NotificationPreferenceDTO(
                preference.getId(),
                preference.getCustomer().getId(), // CustomerId is part of NotificationPreferenceDTO
                preference.isEmailOptIn(),
                preference.isSmsOptIn(),
                preference.isPromoOptIn()
        );
    }

    public static NotificationStatusDTO toNotificationStatusDTO(NotificationStatus status) {
        // Ensure this matches the NotificationStatusDTO constructor: NotificationStatusDTO(Long id, String channel, String status, String messageId, LocalDateTime timestamp)
        return new NotificationStatusDTO(
                status.getId(),
                status.getChannel().name(), // Convert enum to String
                status.getStatus().name(),   // Convert enum to String
                status.getMessageId(),
                status.getTimestamp()
        );
    }
}
