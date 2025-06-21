package com.example.customer_notification_system.service;

import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.requests.RegisterUserRequest; // Make sure to import this
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List; // If you have methods returning List<CustomerDTO>

public interface CustomerService {
    // Existing methods (add or ensure these exist based on your project)
    CustomerDTO createCustomer(CreateCustomerRequest request);
    CustomerDTO getCustomerById(Long id);
    CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request);
    void deleteCustomer(Long id);
    Page<CustomerDTO> getAllCustomers(Pageable pageable);
    Page<CustomerDTO> searchCustomers(String query, Pageable pageable);
    List<CustomerDTO> getAllCustomers(); // Example, if you have a non-paginated version

    // New method for user registration
    CustomerDTO registerNewCustomer(RegisterUserRequest request);
}