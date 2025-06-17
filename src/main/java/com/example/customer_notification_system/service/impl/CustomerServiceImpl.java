package com.example.customer_notification_system.service.impl;

import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;
import com.example.customer_notification_system.entity.Customer;
import com.example.customer_notification_system.repository.CustomerRepository;
import com.example.customer_notification_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    @Override
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(this::toDTO).orElseThrow();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request) {
        return null;
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CreateCustomerRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        return toDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String query) {
        return customerRepository.findByFullNameContainingIgnoreCase(query)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private CustomerDTO toDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        return dto;
    }
}