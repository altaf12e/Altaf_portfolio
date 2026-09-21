package com.portfolio.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "blog_posts")
public class BlogPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @Column(unique = true)
    private String slug;

    @Column(length = 1500)
    private String summary;

    @Column(length = 15000)
    private String content;

    private String tags; // e.g. "Spring Boot, Java 21, Performance"
    private int readTimeMinutes = 5;
    private LocalDate publishedDate = LocalDate.now();
    private int viewCount = 0;
    private boolean published = true;

    public BlogPost() {
    }

    public BlogPost(String title, String summary, String content, String tags, int readTimeMinutes) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.tags = tags;
        this.readTimeMinutes = readTimeMinutes;
        this.publishedDate = LocalDate.now();
        this.published = true;
        this.slug = generateSlug(title);
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getReadTimeMinutes() {
        return readTimeMinutes;
    }

    public void setReadTimeMinutes(int readTimeMinutes) {
        this.readTimeMinutes = readTimeMinutes;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
