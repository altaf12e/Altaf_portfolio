package com.portfolio.app.repository;

import com.portfolio.app.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    List<BlogPost> findByPublishedTrueOrderByPublishedDateDesc();

    List<BlogPost> findAllByOrderByPublishedDateDesc();

    Optional<BlogPost> findBySlug(String slug);

    @Modifying
    @Transactional
    @Query("UPDATE BlogPost b SET b.viewCount = b.viewCount + 1 WHERE b.id = :id")
    int incrementViews(@Param("id") Long id);
}

