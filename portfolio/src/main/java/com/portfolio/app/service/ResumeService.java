package com.portfolio.app.service;

import com.portfolio.app.model.ResumeDocument;
import com.portfolio.app.repository.ResumeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Transactional
    public Optional<ResumeDocument> getLatestResume() {
        Optional<ResumeDocument> opt = resumeRepository.findTopByOrderByUploadedAtDesc();
        if (opt.isPresent()) {
            ResumeDocument doc = opt.get();
            if (!isOldDummyResume(doc.getData())) {
                return opt;
            }
            // Existing doc is dummy or empty, replace with real PDF
            byte[] realBytes = loadRealResumeBytes();
            if (realBytes.length > 0) {
                doc.setFileName("Altaf_Hussain_Resume.pdf");
                doc.setContentType("application/pdf");
                doc.setFileSize((long) realBytes.length);
                doc.setData(realBytes);
                doc.setUploadedAt(LocalDateTime.now());
                resumeRepository.save(doc);
                log.info("Replaced dummy resume with real Altaf_Hussain_Resume.pdf ({} bytes)", realBytes.length);
                return Optional.of(doc);
            }
            return opt;
        }

        // None present: seed default real resume
        byte[] realBytes = loadRealResumeBytes();
        if (realBytes.length > 0) {
            ResumeDocument doc = new ResumeDocument(
                    "Altaf_Hussain_Resume.pdf",
                    "application/pdf",
                    (long) realBytes.length,
                    realBytes
            );
            return Optional.of(resumeRepository.save(doc));
        }

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public boolean hasResume() {
        return resumeRepository.count() > 0 || loadRealResumeBytes().length > 0;
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
     * Initialize default resume seed if no document exists yet or if existing is a dummy placeholder.
     */
    @Transactional
    public void initDefaultResumeIfNotPresent() {
        Optional<ResumeDocument> existing = resumeRepository.findTopByOrderByUploadedAtDesc();
        boolean needsSeed = existing.isEmpty() || isOldDummyResume(existing.get().getData());

        if (needsSeed) {
            byte[] realPdf = loadRealResumeBytes();
            if (realPdf.length == 0) {
                realPdf = createDefaultPdfBytes("Altaf Hussain");
            }

            ResumeDocument doc = existing.orElseGet(ResumeDocument::new);
            doc.setFileName("Altaf_Hussain_Resume.pdf");
            doc.setContentType("application/pdf");
            doc.setFileSize((long) realPdf.length);
            doc.setData(realPdf);
            doc.setUploadedAt(LocalDateTime.now());
            resumeRepository.save(doc);
            log.info("Default initial real resume seeded successfully ({} bytes).", realPdf.length);
        }
    }

    private boolean isOldDummyResume(byte[] data) {
        if (data == null || data.length == 0) return true;
        if (data.length < 1500) {
            String s = new String(data, StandardCharsets.ISO_8859_1);
            return s.contains("(Full Stack Developer | Spring Boot & Java Engineer)")
                    || s.contains("Altaf Hussain - Resume");
        }
        return false;
    }

    private byte[] loadRealResumeBytes() {
        String[] possiblePaths = {
                "static/Resume.pdf",
                "static/images/Resume.pdf",
                "Resume.pdf"
        };
        for (String path : possiblePaths) {
            byte[] b = loadResourceBytes(path);
            if (b.length > 5000) {
                return b;
            }
        }
        return new byte[0];
    }

    private byte[] loadResourceBytes(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return is.readAllBytes();
                }
            }
            File f = new File("src/main/resources/" + path);
            if (f.exists()) {
                try (FileInputStream fis = new FileInputStream(f)) {
                    return fis.readAllBytes();
                }
            }
            File f2 = new File("portfolio/src/main/resources/" + path);
            if (f2.exists()) {
                try (FileInputStream fis = new FileInputStream(f2)) {
                    return fis.readAllBytes();
                }
            }
        } catch (Exception e) {
            log.warn("Could not load resume bytes from path {}: {}", path, e.getMessage());
        }
        return new byte[0];
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
