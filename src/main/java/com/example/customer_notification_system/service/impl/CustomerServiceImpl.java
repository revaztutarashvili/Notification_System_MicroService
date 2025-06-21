package com.example.customer_notification_system.service.impl;

import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.requests.RegisterUserRequest;
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;
import com.example.customer_notification_system.entity.Customer;
import com.example.customer_notification_system.exception.DuplicateResourceException;
import com.example.customer_notification_system.exception.ResourceNotFoundException;
import com.example.customer_notification_system.repository.CustomerRepository;
import com.example.customer_notification_system.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
        }
        Customer customer = modelMapper.map(request, Customer.class);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        // Role assignment for customers removed as per request to remove ROLE_USER
        Customer savedCustomer = customerRepository.save(customer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
        return modelMapper.map(customer, CustomerDTO.class);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));

        if (request.getUsername() != null && !request.getUsername().equals(existingCustomer.getUsername())) {
            if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
            }
            existingCustomer.setUsername(request.getUsername());
        }
        if (request.getPassword() != null) {
            existingCustomer.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFullName() != null) {
            existingCustomer.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            existingCustomer.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(request.getPhoneNumber());
        }

        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return modelMapper.map(updatedCustomer, CustomerDTO.class);
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        Page<Customer> customersPage = customerRepository.findAll(pageable);
        return customersPage.map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    @Override
    public Page<CustomerDTO> searchCustomers(String query, Pageable pageable) {
        Page<Customer> customersPage = customerRepository.findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(query, query, pageable);
        return customersPage.map(customer -> modelMapper.map(customer, CustomerDTO.class));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO registerNewCustomer(RegisterUserRequest request) {
        if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
        }

        Customer newCustomer = new Customer();
        newCustomer.setUsername(request.getUsername());
        newCustomer.setPassword(passwordEncoder.encode(request.getPassword()));
        newCustomer.setFullName(request.getFullName());
        newCustomer.setEmail(request.getEmail());
        newCustomer.setPhoneNumber(request.getPhoneNumber());
        // Role assignment for new registered customers removed as per request to remove ROLE_USER
        // The 'role' field in Customer entity can still exist but will not be set to 'ROLE_USER' from the enum.
        // It will be null by default, or an empty string, depending on your database schema and default values.

        Customer savedCustomer = customerRepository.save(newCustomer);
        return modelMapper.map(savedCustomer, CustomerDTO.class);
    }
}