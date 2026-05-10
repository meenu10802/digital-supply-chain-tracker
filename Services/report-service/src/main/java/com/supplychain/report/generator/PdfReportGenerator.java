package com.supplychain.report.generator;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Component("pdf")
public class PdfReportGenerator implements ReportGenerator {

    @Override
    public byte[] generate(String reportType, List<?> data) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font bodyFont = new Font(Font.FontFamily.HELVETICA, 10);

        document.add(new Paragraph("Supply Chain — " + reportType + " Report", titleFont));
        document.add(new Paragraph("Generated: " + LocalDateTime.now(), bodyFont));
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Total Records: " + data.size(), headerFont));
        document.add(Chunk.NEWLINE);

        for (Object item : data) {
            document.add(new Paragraph(item.toString(), bodyFont));
        }

        document.close();
        return out.toByteArray();
    }

    @Override
    public String getContentType() {
        return "application/pdf";
    }

    @Override
    public String getFileExtension() {
        return ".pdf";
    }
}