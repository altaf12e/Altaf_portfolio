package com.portfolio.app.service;

import com.portfolio.app.model.Certificate;
import com.portfolio.app.repository.CertificateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private static final Logger log = LoggerFactory.getLogger(CertificateService.class);

    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Transactional(readOnly = true)
    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAllByOrderByUploadedAtDesc();
    }

    @Transactional(readOnly = true)
    public Optional<Certificate> getCertificateById(Long id) {
        return certificateRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public long getTotalCertificatesCount() {
        return certificateRepository.count();
    }

    /**
     * Save or update certificate with optional file upload (PDF or Image).
     */
    @Transactional
    public Certificate saveCertificate(Certificate cert, MultipartFile file) throws IOException, IllegalArgumentException {
        if (file != null && !file.isEmpty()) {
            validateCertificateFile(file);

            cert.setFileName(file.getOriginalFilename());
            cert.setFileType(determineContentType(file));
            cert.setFileSize(file.getSize());
            cert.setFileData(file.getBytes());
            cert.setUploadedAt(LocalDateTime.now());
        } else if (cert.getId() != null) {
            // Retain existing file data if updating without a new file
            certificateRepository.findById(cert.getId()).ifPresent(existing -> {
                cert.setFileName(existing.getFileName());
                cert.setFileType(existing.getFileType());
                cert.setFileSize(existing.getFileSize());
                cert.setFileData(existing.getFileData());
                cert.setUploadedAt(existing.getUploadedAt());
            });
        }

        if (cert.getUploadedAt() == null) {
            cert.setUploadedAt(LocalDateTime.now());
        }

        Certificate saved = certificateRepository.save(cert);
        log.info("Certificate saved successfully: '{}' by {} (ID: {})", saved.getTitle(), saved.getIssuingOrganization(), saved.getId());
        return saved;
    }

    @Transactional
    public void deleteCertificate(Long id) {
        certificateRepository.deleteById(id);
        log.info("Certificate deleted with ID: {}", id);
    }

    private void validateCertificateFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Certificate file name cannot be blank.");
        }

        String lower = filename.toLowerCase();
        boolean validExt = lower.endsWith(".pdf") || lower.endsWith(".png")
                || lower.endsWith(".jpg") || lower.endsWith(".jpeg") || lower.endsWith(".webp");

        if (!validExt) {
            throw new IllegalArgumentException("Invalid file format. Only PDF (*.pdf) and images (*.png, *.jpg, *.jpeg, *.webp) are allowed.");
        }
    }

    private String determineContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType != null && !contentType.equalsIgnoreCase("application/octet-stream")) {
            return contentType;
        }

        String filename = file.getOriginalFilename();
        if (filename != null) {
            String lower = filename.toLowerCase();
            if (lower.endsWith(".pdf")) return "application/pdf";
            if (lower.endsWith(".png")) return "image/png";
            if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
            if (lower.endsWith(".webp")) return "image/webp";
        }

        return "application/octet-stream";
    }

    /**
     * Seeds initial certificates if the repository is empty.
     */
    @Transactional
    public void initDefaultCertificatesIfNotPresent() {
        if (certificateRepository.count() == 0) {
            byte[] samplePdf = createSampleCertificatePdf("Oracle Certified Professional", "Altaf Hussain");

            Certificate cert1 = new Certificate(
                    "Oracle Certified Professional: Java SE 17 Developer",
                    "Oracle",
                    "August 2024",
                    "OCP-9482710",
                    "https://catalog-education.oracle.com",
                    "Covers modern Java SE features, OOP, streams & functional programming, concurrency, and virtual threads.",
                    "Oracle_Java_SE_17_Certificate.pdf",
                    "application/pdf",
                    (long) samplePdf.length,
                    samplePdf
            );

            byte[] samplePdf2 = createSampleCertificatePdf("AWS Certified Solutions Architect", "Altaf Hussain");

            Certificate cert2 = new Certificate(
                    "AWS Certified Solutions Architect - Associate",
                    "Amazon Web Services (AWS)",
                    "May 2024",
                    "AWS-SAA-839201",
                    "https://aws.amazon.com/verification",
                    "Demonstrates expertise in distributed cloud architectures, high availability, security, VPCs, and auto-scaling.",
                    "AWS_Solutions_Architect_Certificate.pdf",
                    "application/pdf",
                    (long) samplePdf2.length,
                    samplePdf2
            );

            Certificate cert3 = new Certificate(
                    "Spring Boot & Microservices Masterclass",
                    "Udemy / Java Academy",
                    "January 2024",
                    "UC-59281938",
                    "https://www.udemy.com/certificate/UC-59281938/",
                    "Comprehensive hands-on engineering of Spring Boot 3, Spring Data JPA, Spring Security, RESTful APIs, and Docker.",
                    "Spring_Boot_Microservices_Cert.pdf",
                    "application/pdf",
                    (long) samplePdf.length,
                    samplePdf
            );

            certificateRepository.saveAll(List.of(cert1, cert2, cert3));
            log.info("Default initial certificates seeded successfully (3 records).");
        }
    }

    private byte[] createSampleCertificatePdf(String certName, String recipientName) {
        String pdf = "%PDF-1.4\n" +
                "1 0 obj << /Type /Catalog /Pages 2 0 R >> endobj\n" +
                "2 0 obj << /Type /Pages /Kids [3 0 R] /Count 1 >> endobj\n" +
                "3 0 obj << /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >> endobj\n" +
                "4 0 obj << /Length 150 >> stream\n" +
                "BT\n" +
                "/F1 20 Tf\n" +
                "72 700 Td\n" +
                "(CERTIFICATE OF ACHIEVEMENT) Tj\n" +
                "/F1 14 Tf\n" +
                "0 -36 Td\n" +
                "(Awarded to: " + recipientName + ") Tj\n" +
                "/F1 12 Tf\n" +
                "0 -28 Td\n" +
                "(For successfully completing: " + certName + ") Tj\n" +
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
                "0000000444 00000 n \n" +
                "trailer << /Size 6 /Root 1 0 R >>\n" +
                "startxref\n" +
                "513\n" +
                "%%EOF\n";
        return pdf.getBytes(StandardCharsets.US_ASCII);
    }
}

