package com.portfolio.app.service;

import com.portfolio.app.model.Project;
import com.portfolio.app.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Project> getFeaturedProjects() {
        return projectRepository.findByFeaturedTrueOrderByCreatedAtDesc();
    }

    public List<Project> getProjectsByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return getAllProjects();
        }
        return projectRepository.findByCategoryIgnoreCaseOrderByCreatedAtDesc(category);
    }

    public List<Project> searchProjects(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAllProjects();
        }
        return projectRepository.searchProjects(query.trim());
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Optional<Project> getProjectBySlug(String slug) {
        return projectRepository.findBySlug(slug);
    }

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Transactional
    public int likeProject(Long id) {
        projectRepository.incrementLikes(id);
        return projectRepository.findById(id).map(Project::getLikesCount).orElse(0);
    }

    @Transactional
    public void incrementViews(Long id) {
        projectRepository.incrementViews(id);
    }

    public int getTotalLikes() {
        return projectRepository.getTotalLikesCount();
    }

    public long getTotalProjectsCount() {
        return projectRepository.count();
    }
}

