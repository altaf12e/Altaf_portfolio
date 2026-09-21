package com.portfolio.app.controller;

import com.portfolio.app.model.ContactMessage;
import com.portfolio.app.model.Project;
import com.portfolio.app.model.Skill;
import com.portfolio.app.service.ContactService;
import com.portfolio.app.service.ProfileService;
import com.portfolio.app.service.ProjectService;
import com.portfolio.app.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PortfolioApiController {

    private final ProjectService projectService;
    private final SkillService skillService;
    private final ContactService contactService;
    private final ProfileService profileService;

    public PortfolioApiController(ProjectService projectService,
                                  SkillService skillService,
                                  ContactService contactService,
                                  ProfileService profileService) {
        this.projectService = projectService;
        this.skillService = skillService;
        this.contactService = contactService;
        this.profileService = profileService;
    }

    /**
     * Live asynchronous like increment endpoint
     */
    @PostMapping("/projects/{id}/like")
    public ResponseEntity<Map<String, Object>> likeProject(@PathVariable Long id) {
        int updatedLikes = projectService.likeProject(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("projectId", id);
        response.put("likes", updatedLikes);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all projects or filter by category & query
     */
    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q) {
        if (q != null && !q.trim().isEmpty()) {
            return ResponseEntity.ok(projectService.searchProjects(q));
        }
        if (category != null && !category.equalsIgnoreCase("All")) {
            return ResponseEntity.ok(projectService.getProjectsByCategory(category));
        }
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    /**
     * Get a single project
     */
    @GetMapping("/projects/{id}")
    public ResponseEntity<?> getProject(@PathVariable Long id) {
        return projectService.getProjectById(id)
                .map(project -> {
                    projectService.incrementViews(id);
                    return ResponseEntity.ok(project);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get skills grouped by category
     */
    @GetMapping("/skills")
    public ResponseEntity<Map<String, List<Skill>>> getSkills() {
        return ResponseEntity.ok(skillService.getSkillsGroupedByCategory());
    }

    /**
     * AJAX Contact message submission for JSON payloads
     */
    @PostMapping(value = "/contact", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> submitContactJson(@RequestBody(required = false) ContactMessage jsonBody) {
        String name = jsonBody != null ? jsonBody.getName() : null;
        String email = jsonBody != null ? jsonBody.getEmail() : null;
        String subject = jsonBody != null ? jsonBody.getSubject() : null;
        String message = jsonBody != null ? jsonBody.getMessage() : null;
        return saveContactAndRespond(name, email, subject, message);
    }

    /**
     * AJAX & Form Contact message submission for Form / Multipart payloads
     */
    @PostMapping(value = "/contact", consumes = "!application/json")
    public ResponseEntity<Map<String, Object>> submitContactForm(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) String fname,
            @RequestParam(required = false) String femail,
            @RequestParam(required = false) String fsubject,
            @RequestParam(required = false) String fmessage
    ) {
        String finalName = name != null ? name : fname;
        String finalEmail = email != null ? email : femail;
        String finalSubject = subject != null ? subject : fsubject;
        String finalMessage = message != null ? message : fmessage;
        return saveContactAndRespond(finalName, finalEmail, finalSubject, finalMessage);
    }

    private ResponseEntity<Map<String, Object>> saveContactAndRespond(
            String name, String email, String subject, String message) {
        if (name == null || name.trim().isEmpty()) name = "Website Visitor";
        if (email == null || email.trim().isEmpty()) email = "visitor@example.com";
        if (message == null || message.trim().isEmpty()) message = "Inquiry sent from portfolio.";

        ContactMessage msg = new ContactMessage(
                name.trim(),
                email.trim(),
                subject != null && !subject.trim().isEmpty() ? subject.trim() : "General Inquiry",
                message.trim()
        );

        ContactMessage saved = contactService.saveMessage(msg);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Thank you! Your message has been received successfully.");
        response.put("id", saved.getId());
        return ResponseEntity.ok(response);
    }

    /**
     * Platform real-time stats
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProjects", projectService.getTotalProjectsCount());
        stats.put("totalSkills", skillService.getTotalSkillsCount());
        stats.put("totalLikes", projectService.getTotalLikes());
        stats.put("totalVisits", profileService.getProfileSettings().getTotalVisits());
        return ResponseEntity.ok(stats);
    }
}

