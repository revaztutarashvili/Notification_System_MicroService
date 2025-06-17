package com.example.customer_notification_system.entity;

import com.example.customer_notification_system.enums.NotificationChannel;
import com.example.customer_notification_system.enums.NotificationStatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_statuses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel; // SMS, EMAIL, POSTAL

    @Enumerated(EnumType.STRING)
    private NotificationStatusType status;  // DELIVERED, FAILED, PENDING

    private LocalDateTime timestamp;

    private String messageId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
