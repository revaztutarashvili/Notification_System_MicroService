package com.example.customer_notification_system.service;

import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;

import java.util.List;
public interface CustomerService {
    CustomerDTO createCustomer(CreateCustomerRequest request);
    CustomerDTO getCustomerById(Long id);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request);

    CustomerDTO updateCustomer(Long id, CreateCustomerRequest request);

    void deleteCustomer(Long id);
    List<CustomerDTO> searchCustomers(String query);
}
