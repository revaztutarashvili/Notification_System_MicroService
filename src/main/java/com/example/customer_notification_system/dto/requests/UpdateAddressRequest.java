package com.example.customer_notification_system.dto.requests;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull; // Nullable ველებისთვის, თუ საჭიროა, ან წაშალეთ თუ არ გამოიყენება

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressRequest {

    // ვივარაუდოთ, რომ ეს ველებია, რასაც ტიპიურად განაახლებთ მისამართისთვის
    // შეგიძლიათ ველები გახადოთ @NotBlank/@NotNull თქვენი ვალიდაციის მოთხოვნების მიხედვით
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Boolean isDefault; // მისამართის ნაგულისხმევად მონიშვნა

    // სურვილისამებრ: თუ გჭირდებათ მისამართის სხვა მომხმარებელთან დაკავშირება,
    // თუმცა, როგორც წესი, მისამართის ID ერთ მომხმარებელთანაა დაკავშირებული.
    // private Long customerId;
}