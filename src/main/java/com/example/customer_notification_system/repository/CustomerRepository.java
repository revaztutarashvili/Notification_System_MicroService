package com.example.customer_notification_system.repository;

import com.example.customer_notification_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByFullNameContainingIgnoreCase(String fullName);

    List<Customer> findByEmailContainingIgnoreCase(String email);

    List<Customer> findByPhoneNumberContaining(String phoneNumber);
}
