package com.example.customer_notification_system.controller;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Import @PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import @AuthenticationPrincipal
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Registers a new user/customer account. Publicly accessible.
     */
    @PostMapping("/register") // POST /api/customers/register
    @PreAuthorize("permitAll()") // This endpoint is publicly accessible
    public ResponseEntity<CustomerDTO> registerUser(@RequestBody RegisterUserRequest request) {
        CustomerDTO registeredCustomer = customerService.registerUser(request);
        return new ResponseEntity<>(registeredCustomer, HttpStatus.CREATED);
    }

    /**
     * Creates a new customer record. Only accessible by ADMINs.
     */
    @PostMapping // POST /api/customers
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    /**
     * Retrieves a customer's details by their unique ID.
     * Accessible by ADMINs (for any customer) or by USERs (only for their own profile).
     */
    @GetMapping("/{id}") // GET /api/customers/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == authentication.principal.id)")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    /**
     * Updates an existing customer's details by their unique ID.
     * Accessible by ADMINs (for any customer) or by USERs (only for their own profile).
     */
    @PutMapping("/{id}") // PUT /api/customers/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == authentication.principal.id)")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest request, @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    /**
     * Deletes a customer by their unique ID. Only accessible by ADMINs.
     */
    @DeleteMapping("/{id}") // DELETE /api/customers/{id}
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a list of all customers. Only accessible by ADMINs.
     */
    @GetMapping // GET /api/customers
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    // TODO: Add search endpoint with appropriate authorization (likely ADMIN only)
    /*
    @GetMapping("/search") // GET /api/customers/search?query=someName
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String query) {
        return ResponseEntity.ok(customerService.searchCustomers(query));
    }
    */
}
