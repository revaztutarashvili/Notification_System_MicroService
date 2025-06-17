package com.example.customer_notification_system.controller;

import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import com.example.customer_notification_system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // For 201 Created status
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers") // Base path for all customer-related operations
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * Registers a new user/customer account.
     * This endpoint is designed to be publicly accessible (permitAll) for new user registration.
     *
     * @param request The DTO containing registration details (username, password, full name, email, phone).
     * @return ResponseEntity with the registered CustomerDTO and HTTP status 201 CREATED.
     */
    @PostMapping("/register") // POST /api/customers/register
    public ResponseEntity<CustomerDTO> registerUser(@RequestBody RegisterUserRequest request) {
        CustomerDTO registeredCustomer = customerService.registerUser(request);
        return new ResponseEntity<>(registeredCustomer, HttpStatus.CREATED); // Return 201 Created
    }

    /**
     * Creates a new customer record. This might be used by an admin to add customers
     * without full login credentials initially, or if different from 'registerUser'.
     *
     * @param request The DTO containing the details for the new customer.
     * @return ResponseEntity with the created CustomerDTO and HTTP status 200 OK.
     */
    @PostMapping // POST /api/customers
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CreateCustomerRequest request) {
        return ResponseEntity.ok(customerService.createCustomer(request));
    }

    /**
     * Retrieves a customer's details by their unique ID.
     *
     * @param id The unique ID of the customer to retrieve.
     * @return ResponseEntity with the CustomerDTO and HTTP status 200 OK.
     */
    @GetMapping("/{id}") // GET /api/customers/{id}
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    /**
     * Updates an existing customer's details by their unique ID.
     *
     * @param id The unique ID of the customer to update.
     * @param request The DTO containing the updated customer details.
     * @return ResponseEntity with the updated CustomerDTO and HTTP status 200 OK.
     */
    @PutMapping("/{id}") // PUT /api/customers/{id}
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long id, @RequestBody UpdateCustomerRequest request) {
        return ResponseEntity.ok(customerService.updateCustomer(id, request));
    }

    /**
     * Deletes a customer by their unique ID.
     *
     * @param id The unique ID of the customer to delete.
     * @return ResponseEntity with no content and HTTP status 204 No Content if successful.
     */
    @DeleteMapping("/{id}") // DELETE /api/customers/{id}
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves a list of all customers.
     *
     * @return ResponseEntity with a list of CustomerDTOs and HTTP status 200 OK.
     */
    @GetMapping // GET /api/customers
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }
    // TODO: Add search endpoint (e.g., /api/customers/search?query=...)
}
