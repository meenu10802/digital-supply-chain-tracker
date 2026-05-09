package com.supplychain.notification_service.controller;

import com.supplychain.notification_service.entity.NotificationLog;
import com.supplychain.notification_service.repository.NotificationLogRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Notification APIs",
        description = "APIs for managing notifications"
)
public class NotificationController {

    private final NotificationLogRepository notificationLogRepository;

    @GetMapping
    @Operation(
            summary = "Get all notifications"
    )
    public ResponseEntity<List<NotificationLog>>
    getAllNotifications() {

        return ResponseEntity.ok(
                notificationLogRepository.findAll()
        );
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get notification by ID"
    )
    public ResponseEntity<NotificationLog>
    getNotificationById(
            @PathVariable Long id
    ) {

        return notificationLogRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/read/{id}")
    @Operation(
            summary = "Mark notification as read"
    )
    public ResponseEntity<String>
    markAsRead(
            @PathVariable Long id
    ) {

        NotificationLog notification =
                notificationLogRepository
                        .findById(id)
                        .orElseThrow();

        notification.setIsRead(true);

        notificationLogRepository.save(notification);

        return ResponseEntity.ok(
                "Notification marked as read"
        );
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete notification"
    )
    public ResponseEntity<String>
    deleteNotification(
            @PathVariable Long id
    ) {

        notificationLogRepository.deleteById(id);

        return ResponseEntity.ok(
                "Notification deleted successfully"
        );
    }
}