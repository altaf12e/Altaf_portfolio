package com.portfolio.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profile_settings")
public class ProfileSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName = "Altaf Hussain";
    private String title = "Senior Full Stack & Java Engineer";
    private String tagline = "Building high-performance backend microservices and modern responsive web systems with Spring Boot.";

    @Column(length = 4000)
    private String aboutText = "I'm a passionate Software Engineer specialized in designing and building scalable, reliable distributed systems and elegant web applications. With deep expertise in Java, Spring Boot, microservices architecture, and clean full-stack design, I help turn complex architectural challenges into robust production solutions.";

    private String email = "altaf.developer@example.com";
    private String phone = "+1 (555) 234-5678";
    private String location = "San Francisco, CA (Open to Remote)";
    private String githubUrl = "https://github.com";
    private String linkedinUrl = "https://linkedin.com";
    private String twitterUrl = "https://twitter.com";
    private String resumeUrl = "/resume";

    private boolean availableForHire = true;
    private long totalVisits = 1420;
    private String avatarUrl = "/images/altaf.jpeg";

    public ProfileSettings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getAboutText() {
        return aboutText;
    }

    public void setAboutText(String aboutText) {
        this.aboutText = aboutText;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public boolean isAvailableForHire() {
        return availableForHire;
    }

    public void setAvailableForHire(boolean availableForHire) {
        this.availableForHire = availableForHire;
    }

    public long getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(long totalVisits) {
        this.totalVisits = totalVisits;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
