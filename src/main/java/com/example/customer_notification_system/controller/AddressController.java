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

    /**
     * Creates a new address for a specific customer.
     * Accessible by ADMINs (to add addresses for any customer).
     *
     * @param request The DTO containing address details and the associated customer ID.
     * @return ResponseEntity with the created AddressDTO and HTTP status 200 OK.
     */
    @PostMapping // POST /api/addresses
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can add addresses
    public ResponseEntity<AddressDTO> addAddress(@RequestBody CreateAddressRequest request) {
        return ResponseEntity.ok(addressService.addAddress(request));
    }

    /**
     * Retrieves all addresses for a specific customer.
     * Accessible by ADMINs (for any customer).
     *
     * @param customerId The ID of the customer whose addresses are to be retrieved.
     * @return ResponseEntity with a list of AddressDTOs and HTTP status 200 OK.
     */
    @GetMapping("/customer/{customerId}") // GET /api/addresses/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can view addresses
    public ResponseEntity<List<AddressDTO>> getAddressesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }

    /**
     * Deletes an address by its ID.
     * Accessible by ADMINs (checking if the address belongs to the current user is no longer applicable for ROLE_USER).
     *
     * @param id The unique ID of the address to be deleted.
     * @return ResponseEntity with no content and HTTP status 204 No Content if successful.
     */
    @DeleteMapping("/{id}") // DELETE /api/addresses/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')") // Only admins can delete addresses
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    // TODO: Add an update endpoint for addresses with appropriate authorization
    // Example:
    /*
    @PutMapping("/{id}") // PUT /api/addresses/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody UpdateAddressRequest request) {
        // You would need to create an UpdateAddressRequest DTO and corresponding service method
        return ResponseEntity.ok(addressService.updateAddress(id, request));
    }
    */
}