package com.portfolio.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Skill name is required")
    @Column(nullable = false)
    private String name;

    @Min(0)
    @Max(100)
    private int proficiency = 80; // 0-100%

    @NotBlank(message = "Category is required")
    private String category = "Backend"; // "Backend", "Frontend", "Database", "DevOps & Cloud", "Core Tools"

    private String iconClass = "fa-solid fa-code";

    private int yearsOfExperience = 3;

    private boolean featured = true;

    public Skill() {
    }

    public Skill(String name, int proficiency, String category, String iconClass, int yearsOfExperience, boolean featured) {
        this.name = name;
        this.proficiency = proficiency;
        this.category = category;
        this.iconClass = iconClass;
        this.yearsOfExperience = yearsOfExperience;
        this.featured = featured;
    }

    public Skill(String name, int proficiency, String category) {
        this(name, proficiency, category, "fa-solid fa-code", 2, true);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProficiency() {
        return proficiency;
    }

    public void setProficiency(int proficiency) {
        this.proficiency = proficiency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIconClass() {
        return iconClass;
    }

    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }

    public String getIcon() {
        return iconClass;
    }

    public void setIcon(String icon) {
        this.iconClass = icon;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public int getYears() {
        return yearsOfExperience;
    }

    public void setYears(int years) {
        this.yearsOfExperience = years;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }
}