package com.supplychain.notification_service.service.impl;

import com.supplychain.notification_service.dto.NotificationResponseDto;
import com.supplychain.notification_service.entity.NotificationLog;
import com.supplychain.notification_service.exception.ResourceNotFoundException;
import com.supplychain.notification_service.repository.NotificationLogRepository;
import com.supplychain.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationLogRepository notificationLogRepository;

    @Override
    public Page<NotificationResponseDto> getAllNotifications(
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return notificationLogRepository
                .findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    public NotificationResponseDto getNotificationById(Long id) {

        NotificationLog notification =
                notificationLogRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: " + id
                                ));

        return mapToDto(notification);
    }

    @Override
    public void markAsRead(Long id) {

        NotificationLog notification =
                notificationLogRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: " + id
                                ));

        notification.setIsRead(true);

        notificationLogRepository.save(notification);
    }

    @Override
    public long getUnreadCount() {

        return notificationLogRepository.countByIsReadFalse();
    }

    private NotificationResponseDto mapToDto(
            NotificationLog notification
    ) {

        return NotificationResponseDto.builder()

                .id(notification.getId())

                .recipientEmail(notification.getRecipientEmail())

                .subject(notification.getSubject())

                .message(notification.getMessage())

                .notificationType(notification.getNotificationType())

                .status(notification.getStatus())

                .isRead(notification.getIsRead())

                .sentAt(notification.getSentAt())

                .createdAt(notification.getCreatedAt())

                .build();
    }
}