package com.supplychain.report.generator;

import java.util.List;

public interface ReportGenerator {
    byte[] generate(String reportType, List<?> data) throws Exception;
    String getContentType();
    String getFileExtension();
}