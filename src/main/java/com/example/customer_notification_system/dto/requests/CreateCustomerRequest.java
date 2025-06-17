package com.example.customer_notification_system.dto.requests;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateCustomerRequest {

    @NotBlank
    private String fullName;

    @Email
    private String email;

    @Pattern(regexp = "^[0-9+\\- ]{8,15}$")
    private String phoneNumber;
}
