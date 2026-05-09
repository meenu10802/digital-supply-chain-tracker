package com.supplychain.notification_service.service;

import com.supplychain.notification_service.dto.NotificationResponseDto;
import org.springframework.data.domain.Page;

public interface NotificationService {

    Page<NotificationResponseDto> getAllNotifications(
            int page,
            int size
    );

    NotificationResponseDto getNotificationById(Long id);

    void markAsRead(Long id);

    long getUnreadCount();
}