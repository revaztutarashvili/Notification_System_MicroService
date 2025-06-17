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

    @Column(unique = true, nullable = false) // New: Username for login, must be unique and not null
    private String username;

    @Column(nullable = false) // New: Hashed password for login, must not be null
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true) // Email should ideally be unique for customer records
    private String email;

    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private NotificationPreference notificationPreference;
}
