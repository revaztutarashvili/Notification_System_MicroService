package com.example.customer_notification_system.mapper;

import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime; // Required for NotificationStatusDTO mapping
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.entity.*;


public class EntityMapper {

    // Existing: Customer mapping
    public static CustomerDTO toCustomerDTO(Customer customer) {
        if (customer == null) return null;

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
                customer.getUsername(), // Map username from entity to DTO
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                addressDTOs,
                preferenceDTO
        );
    }

    // Existing: Address mapping
    public static AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;

        return new AddressDTO(
                address.getId(),
                address.getType() != null ? address.getType().name() : null, // enum to String
                address.getValue()
        );
    }

    // Existing: NotificationPreference mapping
    public static NotificationPreferenceDTO toPreferenceDTO(NotificationPreference preference) {
        if (preference == null) return null;
        return new NotificationPreferenceDTO(
                preference.getId(),
                preference.getCustomer() != null ? preference.getCustomer().getId() : null, // CustomerId is part of NotificationPreferenceDTO
                preference.isEmailOptIn(),
                preference.isSmsOptIn(),
                preference.isPromoOptIn()
        );
    }

    // Existing: NotificationStatus mapping
    public static NotificationStatusDTO toNotificationStatusDTO(NotificationStatus status) {
        if (status == null) return null;
        return new NotificationStatusDTO(
                status.getId(),
                status.getChannel() != null ? status.getChannel().name() : null, // Convert enum to String
                status.getStatus() != null ? status.getStatus().name() : null,   // Convert enum to String
                status.getMessageId(),
                status.getTimestamp()
        );
    }

    // NEW: Admin mapping - შესწორებული role-ის გადასაცემად
    public static AdminDTO toAdminDTO(Admin admin) {
        if (admin == null) return null;
        return new AdminDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getRole() // დაამატეთ ეს არგუმენტი
        );
    }
}