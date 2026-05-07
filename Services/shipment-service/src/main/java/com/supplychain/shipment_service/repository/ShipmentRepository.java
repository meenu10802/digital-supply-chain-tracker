package com.supplychain.shipment_service.repository;

import com.supplychain.shipment_service.entity.Shipment;
import com.supplychain.shipment_service.enums.ShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    List<Shipment> findBySupplierId(Long supplierId);

    List<Shipment> findByTransporterId(Long transporterId);

    List<Shipment> findByCurrentStatus(ShipmentStatus status);

    @Query("""
        SELECT s FROM Shipment s
        WHERE s.expectedDelivery < :currentTime
        AND s.currentStatus <> 'DELIVERED'
        AND s.currentStatus <> 'CANCELLED'
    """)
    List<Shipment> findDelayedShipments(LocalDateTime currentTime);
}