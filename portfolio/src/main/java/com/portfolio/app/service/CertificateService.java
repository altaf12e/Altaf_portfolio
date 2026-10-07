package com.portfolio.app.service;

import com.portfolio.app.model.Certificate;
import com.portfolio.app.repository.CertificateRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    private static final Logger log = LoggerFactory.getLogger(CertificateService.class);

    private final CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Transactional
    public List<Certificate> getAllCertificates() {
        List<Certificate> list = certificateRepository.findAllByOrderByUploadedAtDesc();
        // Check if list is empty or contains old dummy certificates
        boolean hasDummy = list.isEmpty() || list.stream().anyMatch(c ->
                c.getTitle() != null && (c.getTitle().contains("Java SE 17 Developer")
                        || c.getTitle().contains("AWS Certified Solutions")
                        || c.getTitle().contains("Spring Boot & Microservices Masterclass")
                        || c.getTitle().contains("Add Your Certificate Title"))
        );

        if (hasDummy) {
            initDefaultCertificatesIfNotPresent();
            return certificateRepository.findAllByOrderByUploadedAtDesc();
        }
        return list;
    }

    @Transactional
    public Optional<Certificate> getCertificateById(Long id) {
        Optional<Certificate> opt = certificateRepository.findById(id);
        if (opt.isPresent()) {
            Certificate c = opt.get();
            // Fallback: If fileData is missing, load it from classpath/static
            if (c.getFileData() == null || c.getFileData().length == 0) {
                byte[] bytes = loadCertificateBytesForFilename(c.getFileName());
                if (bytes.length > 0) {
                    c.setFileData(bytes);
                    c.setFileSize((long) bytes.length);
                    certificateRepository.save(c);
                }
            }
        }
        return opt;
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
     * Seeds initial certificates if the repository is empty or has old dummy data.
     */
    @Transactional
    public void initDefaultCertificatesIfNotPresent() {
        boolean needsUpdate = false;
        if (certificateRepository.count() == 0) {
            needsUpdate = true;
        } else {
            List<Certificate> current = certificateRepository.findAll();
            for (Certificate c : current) {
                if (c.getTitle() != null && (c.getTitle().contains("Java SE 17 Developer")
                        || c.getTitle().contains("AWS Certified Solutions")
                        || c.getTitle().contains("Spring Boot & Microservices Masterclass")
                        || c.getTitle().contains("Add Your Certificate Title")
                        || c.getTitle().contains("Temporary Cert"))) {
                    needsUpdate = true;
                    break;
                }
            }
        }

        if (needsUpdate) {
            certificateRepository.deleteAll();

            byte[] trainingPdf = loadCertificateBytesForFilename("TrainingCertificate.pdf");
            byte[] internPdf = loadCertificateBytesForFilename("InternCertificate.pdf");
            byte[] oracleImg = loadCertificateBytesForFilename("Certificate.jpeg");

            Certificate cert1 = new Certificate(
                    "Java Spring Boot Summer Training",
                    "Techpile Technology Pvt. Ltd., Lucknow",
                    "2026",
                    "TECHPILE-2026-A++",
                    "https://www.techpile.in",
                    "Completed a 45-day intensive Summer Training in Java Spring Boot, covering REST APIs, MVC architecture, database integration, and project development.",
                    "TrainingCertificate.pdf",
                    "application/pdf",
                    trainingPdf.length > 0 ? (long) trainingPdf.length : 899780L,
                    trainingPdf.length > 0 ? trainingPdf : createSampleCertificatePdf("Java Spring Boot Summer Training", "Altaf Hussain")
            );

            Certificate cert2 = new Certificate(
                    "Web Development Internship",
                    "Online / Remote",
                    "Nov – Dec 2025",
                    "INTERN-2025-WEB",
                    "https://www.codealpha.tech",
                    "Completed a full-stack web development internship working with HTML, CSS, JavaScript, Node.js, Express.js, and MongoDB to build production-ready features.",
                    "InternCertificate.pdf",
                    "application/pdf",
                    internPdf.length > 0 ? (long) internPdf.length : 184927L,
                    internPdf.length > 0 ? internPdf : createSampleCertificatePdf("Web Development Internship", "Altaf Hussain")
            );

            Certificate cert3 = new Certificate(
                    "Oracle Cloud Infrastructure 2025 Certified AI Foundations Associate",
                    "Oracle University",
                    "October 01, 2025",
                    "102799019OCI25AICFA",
                    "https://education.oracle.com",
                    "Demonstrates foundational knowledge of Artificial Intelligence, Machine Learning, Deep Learning, and Generative AI services on Oracle Cloud Infrastructure.",
                    "Certificate.jpeg",
                    "image/jpeg",
                    oracleImg.length > 0 ? (long) oracleImg.length : 57774L,
                    oracleImg.length > 0 ? oracleImg : createSampleCertificatePdf("Oracle Cloud Infrastructure 2025 Certified AI Foundations Associate", "Altaf Hussain")
            );

            certificateRepository.saveAll(List.of(cert1, cert2, cert3));
            log.info("Vercel-matched real certificates seeded successfully (3 records).");
        }
    }

    private byte[] loadCertificateBytesForFilename(String filename) {
        if (filename == null || filename.isBlank()) return new byte[0];

        String[] candidatePaths = {
                "static/images/" + filename,
                "static/" + filename,
                "images/" + filename
        };

        for (String path : candidatePaths) {
            byte[] bytes = loadResourceBytes(path);
            if (bytes.length > 0) return bytes;
        }

        if (filename.equalsIgnoreCase("Certificate.jpeg")) {
            byte[] bytes = loadResourceBytes("static/images/Certificate .jpeg");
            if (bytes.length > 0) return bytes;
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
            log.warn("Could not load resource bytes from path {}: {}", path, e.getMessage());
        }
        return new byte[0];
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
