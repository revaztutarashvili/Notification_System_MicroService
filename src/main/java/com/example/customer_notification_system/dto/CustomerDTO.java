package com.example.customer_notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private List<AddressDTO> addresses;
    private NotificationPreferenceDTO notificationPreference;


}
