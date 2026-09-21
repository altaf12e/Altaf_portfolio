package com.portfolio.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Certificate title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Issuing organization is required")
    @Column(nullable = false)
    private String issuingOrganization;

    @NotBlank(message = "Issue date is required")
    private String issueDate;

    private String expirationDate;

    private String credentialId;

    @Column(length = 500)
    private String credentialUrl;

    @Column(length = 1000)
    private String description;

    private String fileName;

    private String fileType;

    private Long fileSize;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "file_data", columnDefinition = "BLOB")
    private byte[] fileData;

    private LocalDateTime uploadedAt;

    public Certificate() {
    }

    public Certificate(String title, String issuingOrganization, String issueDate, String credentialId,
                       String credentialUrl, String description, String fileName, String fileType,
                       Long fileSize, byte[] fileData) {
        this.title = title;
        this.issuingOrganization = issuingOrganization;
        this.issueDate = issueDate;
        this.credentialId = credentialId;
        this.credentialUrl = credentialUrl;
        this.description = description;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.fileData = fileData;
        this.uploadedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onSave() {
        if (this.uploadedAt == null) {
            this.uploadedAt = LocalDateTime.now();
        }
    }

    public boolean hasFile() {
        return fileData != null && fileData.length > 0;
    }

    public boolean isPdf() {
        return (fileType != null && fileType.equalsIgnoreCase("application/pdf"))
                || (fileName != null && fileName.toLowerCase().endsWith(".pdf"));
    }

    public boolean isImage() {
        return fileType != null && fileType.startsWith("image/");
    }

    public String getFormattedFileSize() {
        if (fileSize == null || fileSize <= 0) return "0 KB";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return (fileSize / 1024) + " KB";
        return String.format("%.1f MB", fileSize / (1024.0 * 1024.0));
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}

