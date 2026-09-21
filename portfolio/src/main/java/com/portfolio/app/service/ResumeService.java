package com.portfolio.app.service;

import com.portfolio.app.model.ResumeDocument;
import com.portfolio.app.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ResumeService {

    private static final Logger log = LoggerFactory.getLogger(ResumeService.class);

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @Transactional(readOnly = true)
    public Optional<ResumeDocument> getLatestResume() {
        return resumeRepository.findTopByOrderByUploadedAtDesc();
    }

    @Transactional(readOnly = true)
    public boolean hasResume() {
        return resumeRepository.count() > 0;
    }

    /**
     * Upload or replace the resume PDF document.
     * Validates that the uploaded file is a PDF.
     */
    @Transactional
    public ResumeDocument saveOrUpdateResume(MultipartFile file) throws IOException, IllegalArgumentException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please select a PDF file to upload.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files (*.pdf) are allowed.");
        }

        String contentType = file.getContentType();
        if (contentType != null && !contentType.equalsIgnoreCase("application/pdf")
                && !contentType.equalsIgnoreCase("application/x-pdf")
                && !contentType.equalsIgnoreCase("application/octet-stream")) {
            throw new IllegalArgumentException("Invalid file format. Uploaded file is not a valid PDF.");
        }

        byte[] bytes = file.getBytes();
        if (bytes.length == 0) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        // Clean up any existing resume records so only the latest replaces it
        Optional<ResumeDocument> existing = resumeRepository.findTopByOrderByUploadedAtDesc();
        ResumeDocument doc;
        if (existing.isPresent()) {
            doc = existing.get();
            doc.setFileName(originalFilename);
            doc.setContentType("application/pdf");
            doc.setFileSize((long) bytes.length);
            doc.setData(bytes);
            doc.setUploadedAt(LocalDateTime.now());
            log.info("Existing resume replaced with new upload: {} ({} bytes)", originalFilename, bytes.length);
        } else {
            doc = new ResumeDocument(originalFilename, "application/pdf", (long) bytes.length, bytes);
            log.info("New resume uploaded: {} ({} bytes)", originalFilename, bytes.length);
        }

        return resumeRepository.save(doc);
    }

    /**
     * Initialize default resume seed if no document exists yet.
     */
    @Transactional
    public void initDefaultResumeIfNotPresent() {
        if (resumeRepository.count() == 0) {
            byte[] defaultPdf = createDefaultPdfBytes("Altaf Hussain");
            ResumeDocument doc = new ResumeDocument(
                    "Altaf_Hussain_Resume.pdf",
                    "application/pdf",
                    (long) defaultPdf.length,
                    defaultPdf
            );
            resumeRepository.save(doc);
            log.info("Default initial resume seeded successfully.");
        }
    }

    private byte[] createDefaultPdfBytes(String authorName) {
        String pdf = "%PDF-1.4\n" +
                "1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj\n" +
                "2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj\n" +
                "3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >> endobj\n" +
                "4 0 obj << /Length 135 >> stream\n" +
                "BT\n" +
                "/F1 22 Tf\n" +
                "72 720 Td\n" +
                "(" + authorName + " - Resume) Tj\n" +
                "/F1 12 Tf\n" +
                "0 -36 Td\n" +
                "(Full Stack Developer | Spring Boot & Java Engineer) Tj\n" +
                "ET\n" +
                "endstream\n" +
                "endobj\n" +
                "5 0 obj << /Type /Font /Subtype /Type1 /BaseFont /Helvetica >> endobj\n" +
                "xref\n" +
                "0 6\n" +
                "0000000000 65535 f \n" +
                "0000000009 00000 n \n" +
                "0000000058 00000 n \n" +
                "0000000115 00000 n \n" +
                "0000000244 00000 n \n" +
                "0000000429 00000 n \n" +
                "trailer << /Size 6 /Root 1 0 R >>\n" +
                "startxref\n" +
                "498\n" +
                "%%EOF\n";
        return pdf.getBytes(StandardCharsets.US_ASCII);
    }
}

