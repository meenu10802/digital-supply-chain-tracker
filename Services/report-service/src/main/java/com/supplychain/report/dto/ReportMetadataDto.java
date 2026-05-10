package com.supplychain.report.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportMetadataDto {
    private Long id;
    private String reportType;
    private String format;
    private Long generatedByUserId;
    private LocalDateTime generatedAt;
    private String description;
}