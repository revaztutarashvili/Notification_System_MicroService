package com.example.customer_notification_system.service;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.dto.requests.CreateAddressRequest;

import java.util.List;
public interface AddressService {

   public AddressDTO addAddress(CreateAddressRequest request);
    List<AddressDTO> getAddressesByCustomerId(Long customerId);
    void deleteAddress(Long addressId);
    boolean isAddressOwnedBy(Long addressId, Long userId); // New method for authorization checks

}
