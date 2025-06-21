package com.example.customer_notification_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // Make username unique
    private String username; // Added for login/registration

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true) // Email should be unique
    private String email;

    private String phoneNumber;

    private String password; // Added for login/registration (hashed password)

    @Column(nullable = false) // NEW: Add role field for customers
    private String role; // e.g., "ROLE_USER"

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private NotificationPreference notificationPreference;
}