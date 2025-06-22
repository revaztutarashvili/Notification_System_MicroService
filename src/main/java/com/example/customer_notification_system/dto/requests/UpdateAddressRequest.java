package com.example.customer_notification_system.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequest {


    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Boolean isDefault;


}