package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.security.CustomUserDetails; // Import CustomUserDetails
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import @PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import @AuthenticationPrincipal
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses") // Base path for all address-related operations
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;


    @PostMapping // POST /api/addresses
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can add addresses
    public ResponseEntity<AddressDTO> addAddress(@RequestBody CreateAddressRequest request) {
        return ResponseEntity.ok(addressService.addAddress(request));
    }


    @GetMapping("/customer/{customerId}") // GET /api/addresses/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can view addresses
    public ResponseEntity<List<AddressDTO>> getAddressesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }


    @DeleteMapping("/{id}") // DELETE /api/addresses/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can delete addresses
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }


}