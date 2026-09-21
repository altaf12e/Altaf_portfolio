package com.portfolio.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Company is required")
    private String company;

    private String companyUrl;
    private String location;
    private String period; // e.g. "2023 - Present"

    @Column(length = 3000)
    private String description;

    private String technologies; // comma-separated e.g. "Java, Spring Boot, AWS, Docker"
    private int displayOrder = 0;

    public Experience() {
    }

    public Experience(String role, String company, String companyUrl, String location, String period, String description, String technologies, int displayOrder) {
        this.role = role;
        this.company = company;
        this.companyUrl = companyUrl;
        this.location = location;
        this.period = period;
        this.description = description;
        this.technologies = technologies;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
