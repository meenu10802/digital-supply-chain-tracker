// entity/CheckpointLog.java
package com.example.checkpoint.entity;

import com.example.checkpoint.enums.CheckpointStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "checkpoint_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckpointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long shipmentId;

    @Column(nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CheckpointStatus checkpointStatus;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Long loggedByUserId;

    private String loggedByName;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @CreationTimestamp
    private LocalDateTime createdAt;
}