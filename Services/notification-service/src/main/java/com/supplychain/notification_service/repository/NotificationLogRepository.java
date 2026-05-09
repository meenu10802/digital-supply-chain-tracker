package com.supplychain.notification_service.repository;

import com.supplychain.notification_service.entity.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationLogRepository
        extends JpaRepository<NotificationLog, Long> {

    long countByIsReadFalse();
}