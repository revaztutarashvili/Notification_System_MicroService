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
     * Accessible by ADMINs (to add addresses for any customer) or by USERs (to add addresses for themselves).
     *
     * @param request The DTO containing address details and the associated customer ID.
     * @param currentUser The authenticated user's details.
     * @return ResponseEntity with the created AddressDTO and HTTP status 200 OK.
     */
    @PostMapping // POST /api/addresses
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #request.customerId == authentication.principal.id)")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody CreateAddressRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser.getRole().equals("ROLE_USER") && !currentUser.getId().equals(request.getCustomerId())) {
            // This check is redundant due to PreAuthorize, but serves as an explicit safeguard
            return ResponseEntity.status(403).build(); // Forbidden if user tries to add address for another customer
        }
        return ResponseEntity.ok(addressService.addAddress(request));
    }

    /**
     * Retrieves a list of all addresses associated with a given customer ID.
     * Accessible by ADMINs (for any customer) or by USERs (only for their own addresses).
     *
     * @param customerId The ID of the customer whose addresses are to be retrieved.
     * @param currentUser The authenticated user's details.
     * @return ResponseEntity with a list of AddressDTOs and HTTP status 200 OK.
     */
    @GetMapping("/customer/{customerId}") // GET /api/addresses/customer/{customerId}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #customerId == authentication.principal.id)")
    public ResponseEntity<List<AddressDTO>> getAddressesByCustomer(@PathVariable Long customerId, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(addressService.getAddressesByCustomerId(customerId));
    }

    /**
     * Deletes an address by its unique ID.
     * Accessible by ADMINs (to delete any address) or by USERs (only to delete their own addresses).
     * This requires checking if the address belongs to the current user.
     *
     * @param id The unique ID of the address to be deleted.
     * @param currentUser The authenticated user's details.
     * @return ResponseEntity with no content and HTTP status 204 No Content if successful.
     */
    @DeleteMapping("/{id}") // DELETE /api/addresses/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and @addressService.isAddressOwnedBy(#id, authentication.principal.id))")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        // You will need to add a method like 'isAddressOwnedBy' to your AddressService
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    // TODO: Add an update endpoint for addresses with appropriate authorization
    // Example:
    /*
    @PutMapping("/{id}") // PUT /api/addresses/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and @addressService.isAddressOwnedBy(#id, authentication.principal.id))")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id, @RequestBody UpdateAddressRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        // You would need to create an UpdateAddressRequest DTO and corresponding service method
        return ResponseEntity.ok(addressService.updateAddress(id, request));
    }
    */
}
