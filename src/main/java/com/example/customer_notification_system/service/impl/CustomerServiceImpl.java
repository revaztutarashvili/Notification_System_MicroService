package com.example.customer_notification_system.service.impl;

import com.example.customer_notification_system.dto.requests.CreateCustomerRequest;
import com.example.customer_notification_system.dto.requests.UpdateCustomerRequest;
import com.example.customer_notification_system.dto.requests.RegisterUserRequest; // New import
import com.example.customer_notification_system.dto.CustomerDTO;
import com.example.customer_notification_system.entity.Customer;
import com.example.customer_notification_system.repository.CustomerRepository;
import com.example.customer_notification_system.service.CustomerService;
import com.example.customer_notification_system.mapper.EntityMapper; // Import EntityMapper for consistent mapping
import com.example.customer_notification_system.exception.ResourceNotFoundException; // Import custom exception
import com.example.customer_notification_system.exception.DuplicateResourceException; // Import custom exception
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder; // New import for password encoding

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder; // New: Inject PasswordEncoder

    @Override
    public CustomerDTO registerUser(RegisterUserRequest request) {
        // Check for duplicate username
        if (customerRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' already exists.");
        }
        // Check for duplicate email (if email is also considered unique for login/registration)
        if (request.getEmail() != null && customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' already exists.");
        }

        Customer customer = new Customer();
        customer.setUsername(request.getUsername());
        customer.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password before saving
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        return EntityMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO createCustomer(CreateCustomerRequest request) {
        // This method can be used by an admin to add customer records.
        // If username/password are not provided here, these customers cannot log in.
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        // If username/password are optional or generated for non-login customers, handle here
        // For now, these customers will not have login capabilities if created via this method.
        return EntityMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerRepository.findById(id).map(EntityMapper::toCustomerDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(EntityMapper::toCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(Long id, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Customer not found with ID: " + id)
        );

        // Update fields only if they are provided in the request (partial update)
        if (request.getFullName() != null) {
            customer.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            // Check for duplicate email if updating and email is unique
            if (customerRepository.findByEmail(request.getEmail()).isPresent() &&
                    !customer.getEmail().equals(request.getEmail())) { // Only check if email actually changed
                throw new DuplicateResourceException("Email '" + request.getEmail() + "' already exists.");
            }
            customer.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            customer.setPhoneNumber(request.getPhoneNumber());
        }
        // If username/password are meant to be updatable, you would add logic here
        // For example: if (request.getNewPassword() != null) customer.setPassword(passwordEncoder.encode(request.getNewPassword()));
        // Make sure to consider if username can also be updated and its uniqueness.

        return EntityMapper.toCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String query) {
        // This method currently searches only by fullName.
        // if we need to combine results from multiple find methods if needed
        // For example:
        // List<Customer> byFullName = customerRepository.findByFullNameContainingIgnoreCase(query);
        // List<Customer> byEmail = customerRepository.findByEmailContainingIgnoreCase(query);
        // List<Customer> byPhone = customerRepository.findByPhoneNumberContaining(query);
        // Combine and return distinct results.
        return customerRepository.findByFullNameContainingIgnoreCase(query)
                .stream().map(EntityMapper::toCustomerDTO).collect(Collectors.toList());
    }
}
