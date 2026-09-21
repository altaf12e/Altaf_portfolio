package com.portfolio.app.service;

import com.portfolio.app.model.ProfileSettings;
import com.portfolio.app.repository.ProfileSettingsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {

    private final ProfileSettingsRepository profileSettingsRepository;

    public ProfileService(ProfileSettingsRepository profileSettingsRepository) {
        this.profileSettingsRepository = profileSettingsRepository;
    }

    public ProfileSettings getProfileSettings() {
        return profileSettingsRepository.findAll().stream()
                .findFirst()
                .orElseGet(() -> profileSettingsRepository.save(new ProfileSettings()));
    }

    public ProfileSettings updateProfileSettings(ProfileSettings settings) {
        return profileSettingsRepository.save(settings);
    }

    public void incrementVisitCount() {
        profileSettingsRepository.incrementVisits();
    }
}

