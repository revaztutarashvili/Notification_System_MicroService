package com.example.customer_notification_system.repository;

import com.example.customer_notification_system.entity.NotificationPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Long> {
    Optional<NotificationPreference> findByCustomerId(Long customerId);
}
