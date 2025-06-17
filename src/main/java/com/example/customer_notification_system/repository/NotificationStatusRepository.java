package com.example.customer_notification_system.repository;

import com.example.customer_notification_system.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationStatusRepository extends JpaRepository<NotificationStatus, Long> {

    List<NotificationStatus> findByCustomerId(Long customerId);

    List<NotificationStatus> findByStatus(String status);

    List<NotificationStatus> findByTimestampBetween(LocalDateTime from, LocalDateTime to);
}
