package com.supplychain.report.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReportGeneratorFactory {

    private final Map<String, ReportGenerator> generators;

    @Autowired
    public ReportGeneratorFactory(Map<String, ReportGenerator> generators) {
        this.generators = generators;
    }

    public ReportGenerator getGenerator(String type) {
        ReportGenerator generator = generators.get(type.toLowerCase());
        if (generator == null) {
            throw new IllegalArgumentException("Unsupported report type: " + type + ". Use 'pdf' or 'excel'.");
        }
        return generator;
    }
}