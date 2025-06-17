package com.example.customer_notification_system.entity;

import com.example.customer_notification_system.enums.AddressType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AddressType type;

    private String value;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
