// repository/AlertRepository.java
package com.example.alert.repository;

import com.example.alert.entity.Alert;
import com.example.alert.enums.AlertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    Page<Alert> findAll(Pageable pageable);

    List<Alert> findByShipmentId(Long shipmentId);

    @Query("SELECT a FROM Alert a WHERE a.isResolved = false ORDER BY a.createdAt DESC")
    List<Alert> findAllUnresolved();

    @Query("SELECT a FROM Alert a WHERE a.alertType = :type ORDER BY a.createdAt DESC")
    List<Alert> findByAlertType(@Param("type") AlertType type);

    @Query("SELECT a FROM Alert a WHERE a.shipmentId = :sid ORDER BY a.createdAt DESC")
    List<Alert> findByShipmentIdOrdered(@Param("sid") Long shipmentId);
}