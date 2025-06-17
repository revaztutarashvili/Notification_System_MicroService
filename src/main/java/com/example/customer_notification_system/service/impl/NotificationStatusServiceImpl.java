package com.example.customer_notification_system.service.impl;
import com.example.customer_notification_system.dto.*;
import com.example.customer_notification_system.entity.*;
import com.example.customer_notification_system.repository.*;
import com.example.customer_notification_system.service.*;
import com.example.customer_notification_system.enums.NotificationChannel;
import com.example.customer_notification_system.enums.NotificationStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationStatusServiceImpl implements NotificationStatusService {
    private final NotificationStatusRepository statusRepository;
    private final CustomerRepository customerRepository;

    @Override
    public NotificationStatusDTO trackNotification(Long customerId, String channel, String status, String messageId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer ID: " + customerId + " can't found."));

        NotificationStatus notificationStatus = new NotificationStatus();
        notificationStatus.setCustomer(customer);


        try {
            notificationStatus.setChannel(NotificationChannel.valueOf(channel.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("incorrect channel type: " + channel);
        }


        try {
            notificationStatus.setStatus(NotificationStatusType.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("incorrect status: " + status);
        }

        notificationStatus.setMessageId(messageId);
        notificationStatus.setTimestamp(LocalDateTime.now());

        return toDTO(statusRepository.save(notificationStatus));
    }

    @Override
    public List<NotificationStatusDTO> getStatusByCustomerId(Long customerId) {
        return statusRepository.findByCustomerId(customerId)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<NotificationStatusDTO> getStatusByStatus(String status) {

        return statusRepository.findByStatus(status)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    private NotificationStatusDTO toDTO(NotificationStatus ns) {
        return new NotificationStatusDTO(
                ns.getId(),
                ns.getChannel().name(),
                ns.getStatus().name(),
                ns.getMessageId(),
                ns.getTimestamp()
        );
    }
}
