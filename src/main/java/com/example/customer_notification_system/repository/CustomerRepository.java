package com.example.customer_notification_system.repository;

import com.example.customer_notification_system.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username); // New: Find customer by username for login/registration
    Optional<Customer> findByEmail(String email);       // New: Find customer by email for uniqueness check

    List<Customer> findByFullNameContainingIgnoreCase(String fullName);
    List<Customer> findByEmailContainingIgnoreCase(String email);
    List<Customer> findByPhoneNumberContaining(String phoneNumber);

}
