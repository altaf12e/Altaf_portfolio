package com.portfolio.app.controller;

import com.portfolio.app.model.Certificate;
import com.portfolio.app.model.ContactMessage;
import com.portfolio.app.model.ProfileSettings;
import com.portfolio.app.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class PortfolioController {

    private static final Logger log = LoggerFactory.getLogger(PortfolioController.class);

    private final ProfileService profileService;
    private final ProjectService projectService;
    private final SkillService skillService;
    private final ExperienceService experienceService;
    private final ContactService contactService;
    private final BlogService blogService;
    private final ResumeService resumeService;
    private final CertificateService certificateService;

    public PortfolioController(
            ProfileService profileService,
            ProjectService projectService,
            SkillService skillService,
            ExperienceService experienceService,
            ContactService contactService,
            BlogService blogService,
            ResumeService resumeService,
            CertificateService certificateService
    ) {
        this.profileService = profileService;
        this.projectService = projectService;
        this.skillService = skillService;
        this.experienceService = experienceService;
        this.contactService = contactService;
        this.blogService = blogService;
        this.resumeService = resumeService;
        this.certificateService = certificateService;
    }

    /**
     * Common model attributes added to all templates.
     */
    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        ProfileSettings profile = profileService.getProfileSettings();

        model.addAttribute("profile", profile);
        model.addAttribute("fullName", profile.getFullName());
        model.addAttribute("title", profile.getTitle());
        model.addAttribute("tagline", profile.getTagline());
        model.addAttribute("aboutText", profile.getAboutText());
        model.addAttribute("email", profile.getEmail());
        model.addAttribute("github", profile.getGithubUrl());
        model.addAttribute("linkedin", profile.getLinkedinUrl());
        model.addAttribute("avatarUrl", profile.getAvatarUrl() != null ? profile.getAvatarUrl() : "/images/altaf.jpeg");
        model.addAttribute("currentUri", request.getRequestURI());
    }

    @GetMapping("/")
    public String home(Model model) {
        profileService.incrementVisitCount();

        model.addAttribute("featuredProjects", projectService.getFeaturedProjects());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("featuredSkills", skillService.getFeaturedSkills());
        model.addAttribute("skills", skillService.getAllSkills());
        model.addAttribute("skillsGrouped", skillService.getSkillsGroupedByCategory());
        model.addAttribute("experiences", experienceService.getAllExperiences());
        model.addAttribute("educations", experienceService.getAllEducations());
        model.addAttribute("testimonials", experienceService.getAllTestimonials());
        model.addAttribute("certificates", certificateService.getAllCertificates());
        model.addAttribute("totalProjects", projectService.getTotalProjectsCount());
        model.addAttribute("totalLikes", projectService.getTotalLikes());
        model.addAttribute("totalSkills", skillService.getTotalSkillsCount());
        model.addAttribute("totalCertificates", certificateService.getTotalCertificatesCount());

        if (!model.containsAttribute("contactMessage")) {
            model.addAttribute("contactMessage", new ContactMessage());
        }

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("skillsGrouped", skillService.getSkillsGroupedByCategory());
        model.addAttribute("skills", skillService.getAllSkills());
        model.addAttribute("experiences", experienceService.getAllExperiences());
        model.addAttribute("educations", experienceService.getAllEducations());

        return "about";
    }

    @GetMapping("/projects")
    public String projects(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String q,
            Model model
    ) {
        List<String> categories = Arrays.asList(
                "All",
                "Backend",
                "Fullstack",
                "Cloud / DevOps",
                "AI / Tools"
        );

        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", category != null ? category : "All");

        if (q != null && !q.trim().isEmpty()) {
            model.addAttribute("projects", projectService.searchProjects(q.trim()));
            model.addAttribute("searchQuery", q.trim());
        } else if (category != null && !category.equalsIgnoreCase("All")) {
            model.addAttribute("projects", projectService.getProjectsByCategory(category));
        } else {
            model.addAttribute("projects", projectService.getAllProjects());
        }

        return "projects";
    }

    @GetMapping("/projects/{id}")
    public String projectDetail(@PathVariable Long id, Model model) {
        return projectService.getProjectById(id)
                .map(project -> {
                    projectService.incrementViews(id);
                    model.addAttribute("project", project);
                    return "project-detail";
                })
                .orElse("redirect:/projects");
    }

    @GetMapping("/resume")
    public String resume(Model model) {
        model.addAttribute("skillsGrouped", skillService.getSkillsGroupedByCategory());
        model.addAttribute("experiences", experienceService.getAllExperiences());
        model.addAttribute("educations", experienceService.getAllEducations());
        model.addAttribute("resumeDocument", resumeService.getLatestResume().orElse(null));
        model.addAttribute("hasResume", resumeService.hasResume());

        return "resume";
    }

    @GetMapping("/resume/view")
    public ResponseEntity<Resource> viewResume() {
        return resumeService.getLatestResume()
                .map(doc -> {
                    ByteArrayResource resource = new ByteArrayResource(doc.getData());
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getFileName() + "\"")
                            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                            .contentLength(doc.getFileSize() != null ? doc.getFileSize() : doc.getData().length)
                            .body((Resource) resource);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/resume/download")
    public ResponseEntity<Resource> downloadResume() {
        return resumeService.getLatestResume()
                .map(doc -> {
                    ByteArrayResource resource = new ByteArrayResource(doc.getData());
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFileName() + "\"")
                            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                            .contentLength(doc.getFileSize() != null ? doc.getFileSize() : doc.getData().length)
                            .body((Resource) resource);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/certificates")
    public String certificates(Model model) {
        model.addAttribute("certificates", certificateService.getAllCertificates());
        model.addAttribute("totalCertificates", certificateService.getTotalCertificatesCount());
        return "certificates";
    }

    @GetMapping("/certificates/view/{id}")
    public ResponseEntity<Resource> viewCertificate(@PathVariable Long id) {
        return certificateService.getCertificateById(id)
                .filter(Certificate::hasFile)
                .map(cert -> {
                    ByteArrayResource resource = new ByteArrayResource(cert.getFileData());
                    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    try {
                        if (cert.getFileType() != null && !cert.getFileType().isBlank()) {
                            mediaType = MediaType.parseMediaType(cert.getFileType());
                        }
                    } catch (Exception ignored) {
                    }

                    return ResponseEntity.ok()
                            .contentType(mediaType)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + cert.getFileName() + "\"")
                            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                            .contentLength(cert.getFileSize() != null ? cert.getFileSize() : cert.getFileData().length)
                            .body((Resource) resource);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/certificates/download/{id}")
    public ResponseEntity<Resource> downloadCertificate(@PathVariable Long id) {
        return certificateService.getCertificateById(id)
                .filter(Certificate::hasFile)
                .map(cert -> {
                    ByteArrayResource resource = new ByteArrayResource(cert.getFileData());
                    MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    try {
                        if (cert.getFileType() != null && !cert.getFileType().isBlank()) {
                            mediaType = MediaType.parseMediaType(cert.getFileType());
                        }
                    } catch (Exception ignored) {
                    }

                    return ResponseEntity.ok()
                            .contentType(mediaType)
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + cert.getFileName() + "\"")
                            .header(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate")
                            .contentLength(cert.getFileSize() != null ? cert.getFileSize() : cert.getFileData().length)
                            .body((Resource) resource);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/blog")
    public String blog(Model model) {
        model.addAttribute("posts", blogService.getPublishedArticles());
        return "blog";
    }

    @GetMapping("/blog/{slug}")
    public String blogDetail(@PathVariable String slug, Model model) {
        return blogService.getArticleBySlug(slug)
                .map(post -> {
                    blogService.incrementViews(post.getId());
                    model.addAttribute("post", post);
                    return "blog-detail";
                })
                .orElse("redirect:/blog");
    }

    @GetMapping("/contact")
    public String contactForm(Model model) {
        if (!model.containsAttribute("contactMessage")) {
            model.addAttribute("contactMessage", new ContactMessage());
        }
        return "contact";
    }

    @PostMapping("/contact")
    public String submitContact(
            @Valid @ModelAttribute("contactMessage") ContactMessage contactMessage,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "contact";
        }

        log.info("New contact message from {} <{}>: {}",
                contactMessage.getName(),
                contactMessage.getEmail(),
                contactMessage.getMessage());

        contactService.saveMessage(contactMessage);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Thank you! Your message has been sent successfully. I'll get back to you promptly."
        );

        return "redirect:/contact";
    }
}