// repository/CheckpointLogRepository.java
package com.example.checkpoint.repository;

import com.example.checkpoint.entity.CheckpointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckpointLogRepository extends JpaRepository<CheckpointLog, Long> {

    @Query("SELECT c FROM CheckpointLog c WHERE c.shipmentId = :sid ORDER BY c.timestamp ASC")
    List<CheckpointLog> findTimelineByShipment(@Param("sid") Long shipmentId);

    @Query("SELECT c FROM CheckpointLog c WHERE c.shipmentId = :sid ORDER BY c.timestamp DESC LIMIT 1")
    Optional<CheckpointLog> findLatestByShipment(@Param("sid") Long shipmentId);

    @Query("SELECT c FROM CheckpointLog c WHERE LOWER(c.location) = LOWER(:location) ORDER BY c.timestamp DESC")
    List<CheckpointLog> findByLocation(@Param("location") String location);
}