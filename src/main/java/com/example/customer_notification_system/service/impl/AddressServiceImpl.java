package com.example.customer_notification_system.service.impl;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.CreateAddressRequest;
import com.example.customer_notification_system.entity.*;
import com.example.customer_notification_system.repository.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.enums.AddressType;
import com.example.customer_notification_system.exception.ResourceNotFoundException; // Import custom exception
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    public AddressDTO addAddress(CreateAddressRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID: " + request.getCustomerId() + " not found."));

        Address address = new Address();
        address.setCustomer(customer);

        try {
            address.setType(AddressType.valueOf(request.getType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid address type: " + request.getType() + ". Possible values: " +
                    java.util.Arrays.toString(AddressType.values()));
        }

        address.setValue(request.getValue());
        return toDTO(addressRepository.save(address));
    }

    @Override
    public List<AddressDTO> getAddressesByCustomerId(Long customerId) {
        return addressRepository.findByCustomerId(customerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteAddress(Long addressId) {
        if (!addressRepository.existsById(addressId)) {
            throw new ResourceNotFoundException("Address not found with ID: " + addressId);
        }
        addressRepository.deleteById(addressId);
    }

    @Override
    public boolean isAddressOwnedBy(Long addressId, Long userId) {
        return addressRepository.findById(addressId)
                .map(address -> address.getCustomer().getId().equals(userId))
                .orElse(false);
    }

    private AddressDTO toDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getType() != null ? address.getType().name() : null,
                address.getValue()
        );
    }
}
