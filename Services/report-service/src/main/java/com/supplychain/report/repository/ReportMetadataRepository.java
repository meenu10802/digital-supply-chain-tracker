package com.supplychain.report.repository;

import com.supplychain.report.entity.ReportMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportMetadataRepository extends JpaRepository<ReportMetadata, Long> {
    List<ReportMetadata> findAllByOrderByGeneratedAtDesc();
}