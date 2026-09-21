package com.portfolio.app.repository;

import com.portfolio.app.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findAllByOrderByCategoryAscProficiencyDesc();

    List<Skill> findByCategoryOrderByProficiencyDesc(String category);

    List<Skill> findByFeaturedTrue();

    @Query("SELECT DISTINCT s.category FROM Skill s ORDER BY s.category")
    List<String> findDistinctCategories();
}

