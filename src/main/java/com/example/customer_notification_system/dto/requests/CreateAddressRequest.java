package com.example.customer_notification_system.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateAddressRequest {
    @NotBlank
    private String type;

    @NotBlank
    private String value;

    private Long customerId;
}
