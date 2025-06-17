package com.example.customer_notification_system.mapper;
import java.util.stream.Collectors;
import java.util.List;
import java.time.LocalDateTime;
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
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                addressDTOs,
                preferenceDTO
        );
    }

    public static AddressDTO toAddressDTO(Address address) {
        if (address == null) return null;

        return new AddressDTO(
                address.getId(),
                address.getType() != null ? address.getType().name() : null, // enum to String
                address.getValue()
        );
    }

    public static NotificationPreferenceDTO toPreferenceDTO(NotificationPreference preference) {
        return new NotificationPreferenceDTO(
                preference.getId(),
                preference.getCustomer().getId(),
                preference.isEmailOptIn(),
                preference.isSmsOptIn(),
                preference.isPromoOptIn()
        );
    }

    public static NotificationStatusDTO toNotificationStatusDTO(NotificationStatus status) {

        return new NotificationStatusDTO(
                status.getId(),
                status.getChannel().name(),
                status.getStatus().name(),
                status.getMessageId(),
                status.getTimestamp()
        );
    }
}
