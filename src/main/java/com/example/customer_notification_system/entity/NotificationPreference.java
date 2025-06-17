package com.example.customer_notification_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notification_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean smsOptIn;
    private boolean emailOptIn;
    private boolean promoOptIn;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
