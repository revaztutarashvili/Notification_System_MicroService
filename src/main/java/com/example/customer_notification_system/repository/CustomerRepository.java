package com.example.customer_notification_system.repository;

import com.example.customer_notification_system.entity.Customer;
import org.springframework.data.domain.Page;     // Import Page
import org.springframework.data.domain.Pageable; // Import Pageable
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username); // New: Find customer by username for login/registration
    Optional<Customer> findByEmail(String email);       // New: Find customer by email for uniqueness check

    List<Customer> findByFullNameContainingIgnoreCase(String fullName); // არსებული მეთოდი, თუ კვლავ გამოიყენება პაგინაციის გარეშე
    Page<Customer> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable); // დამატებულია პაგინაციისთვის
    List<Customer> findByEmailContainingIgnoreCase(String email);
    List<Customer> findByPhoneNumberContaining(String phoneNumber);
    Page<Customer> findByUsernameContainingIgnoreCaseOrFullNameContainingIgnoreCase(String username, String fullName, Pageable pageable);
}