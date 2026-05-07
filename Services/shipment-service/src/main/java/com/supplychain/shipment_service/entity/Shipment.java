package com.supplychain.shipment_service.entity;

import com.supplychain.shipment_service.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;

    private String itemName;

    private Long supplierId;

    private Long transporterId;

    private String fromLocation;

    private String toLocation;

    private LocalDateTime expectedDelivery;

    private LocalDateTime actualDelivery;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus currentStatus;

    @Column(length = 1000)
    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}