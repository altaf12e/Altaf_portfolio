package com.portfolio.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "resume_documents")
public class ResumeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    private String contentType;

    private Long fileSize;

    private LocalDateTime uploadedAt;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "pdf_data", columnDefinition = "BLOB")
    private byte[] data;

    public ResumeDocument() {
    }

    public ResumeDocument(String fileName, String contentType, Long fileSize, byte[] data) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileSize = fileSize;
        this.data = data;
        this.uploadedAt = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    public void onSave() {
        this.uploadedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}

