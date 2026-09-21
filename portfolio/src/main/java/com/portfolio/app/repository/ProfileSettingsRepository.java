package com.portfolio.app.repository;

import com.portfolio.app.model.ProfileSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ProfileSettingsRepository extends JpaRepository<ProfileSettings, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ProfileSettings p SET p.totalVisits = p.totalVisits + 1")
    int incrementVisits();
}

