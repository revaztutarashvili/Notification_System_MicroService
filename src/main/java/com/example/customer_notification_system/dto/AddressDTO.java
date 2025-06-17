package com.example.customer_notification_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDTO {
    private Long id;
    private String type;  // EMAIL, SMS, POSTAL - as String
    private String value;
}
