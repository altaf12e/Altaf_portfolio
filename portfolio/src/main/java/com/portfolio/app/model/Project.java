package com.portfolio.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Project title is required")
    @Column(nullable = false)
    private String title;

    private String subtitle;

    @Column(length = 3000)
    private String description;

    @Column(length = 8000)
    private String fullDetails;

    private String category = "Backend";

    private String imageUrl;

    private String githubUrl;

    private String liveUrl;

    private String slug;

    private int likesCount = 0;

    private int viewCount = 0;

    private boolean featured = false;

    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "project_technologies",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "technology")
    private List<String> techStack = new ArrayList<>();

    public Project() {
    }

    public Project(String title,
                   String subtitle,
                   String description,
                   String category,
                   List<String> techStack,
                   String githubUrl,
                   String liveUrl,
                   String imageUrl,
                   boolean featured) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.category = category;
        this.techStack = techStack != null ? new ArrayList<>(techStack) : new ArrayList<>();
        this.githubUrl = githubUrl;
        this.liveUrl = liveUrl;
        this.imageUrl = imageUrl;
        this.featured = featured;
        this.slug = generateSlug(title);
    }

    public Project(String title,
                   String description,
                   String category,
                   List<String> techStack,
                   String githubUrl,
                   String liveUrl,
                   String imageUrl,
                   boolean featured) {
        this(title, null, description, category, techStack, githubUrl, liveUrl, imageUrl, featured);
    }

    @PrePersist
    public void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.slug == null || this.slug.isEmpty()) {
            this.slug = generateSlug(this.title);
        }
    }

    private String generateSlug(String text) {
        if (text == null) return "";
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-");
    }

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
        if (this.slug == null || this.slug.isEmpty()) {
            this.slug = generateSlug(title);
        }
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFullDetails() {
        return fullDetails;
    }

    public void setFullDetails(String fullDetails) {
        this.fullDetails = fullDetails;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLiveUrl() {
        return liveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        this.liveUrl = liveUrl;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getTechStack() {
        return techStack;
    }

    public void setTechStack(List<String> techStack) {
        this.techStack = techStack != null ? techStack : new ArrayList<>();
    }

    /**
     * Alias for backward compatibility with code expecting getTechnologies
     */
    public List<String> getTechnologies() {
        return getTechStack();
    }

    public void setTechnologies(List<String> technologies) {
        setTechStack(technologies);
    }
}