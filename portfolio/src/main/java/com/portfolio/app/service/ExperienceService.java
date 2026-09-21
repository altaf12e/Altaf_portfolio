package com.portfolio.app.service;

import com.portfolio.app.model.Education;
import com.portfolio.app.model.Experience;
import com.portfolio.app.model.Testimonial;
import com.portfolio.app.repository.EducationRepository;
import com.portfolio.app.repository.ExperienceRepository;
import com.portfolio.app.repository.TestimonialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;
    private final TestimonialRepository testimonialRepository;

    public ExperienceService(ExperienceRepository experienceRepository,
                             EducationRepository educationRepository,
                             TestimonialRepository testimonialRepository) {
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
        this.testimonialRepository = testimonialRepository;
    }

    public List<Experience> getAllExperiences() {
        return experienceRepository.findAllByOrderByDisplayOrderAsc();
    }

    public Experience saveExperience(Experience experience) {
        return experienceRepository.save(experience);
    }

    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    public List<Education> getAllEducations() {
        return educationRepository.findAllByOrderByDisplayOrderAsc();
    }

    public Education saveEducation(Education education) {
        return educationRepository.save(education);
    }

    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    public List<Testimonial> getAllTestimonials() {
        return testimonialRepository.findAll();
    }

    public Testimonial saveTestimonial(Testimonial testimonial) {
        return testimonialRepository.save(testimonial);
    }
}

