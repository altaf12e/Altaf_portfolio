package com.portfolio.app.repository;

import com.portfolio.app.model.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByFeaturedTrueOrderByCreatedAtDesc();

    List<Project> findAllByOrderByCreatedAtDesc();

    List<Project> findByCategoryIgnoreCaseOrderByCreatedAtDesc(String category);

    Optional<Project> findBySlug(String slug);

    @Query("""
            SELECT p FROM Project p
            WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))
               OR LOWER(p.category) LIKE LOWER(CONCAT('%', :query, '%'))
            """)
    List<Project> searchProjects(@Param("query") String query);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.likesCount = p.likesCount + 1 WHERE p.id = :id")
    int incrementLikes(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Project p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    int incrementViews(@Param("id") Long id);

    @Query("SELECT COALESCE(SUM(p.likesCount), 0) FROM Project p")
    int getTotalLikesCount();
}