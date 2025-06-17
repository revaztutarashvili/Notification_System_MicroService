package com.example.customer_notification_system.service;

import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.*;
import java.util.List;

public interface CustomerService {
    CustomerDTO registerUser(RegisterUserRequest request); // New:
    CustomerDTO createCustomer(CreateCustomerRequest request);
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request);
    void deleteCustomer(Long id);
    List<CustomerDTO> searchCustomers(String query); }

