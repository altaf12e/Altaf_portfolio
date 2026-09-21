package com.portfolio.app.repository;

import com.portfolio.app.model.ResumeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<ResumeDocument, Long> {

    Optional<ResumeDocument> findTopByOrderByUploadedAtDesc();
}

