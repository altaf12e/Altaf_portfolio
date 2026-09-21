package com.portfolio.app.controller;

import com.portfolio.app.model.Certificate;
import com.portfolio.app.model.Education;
import com.portfolio.app.model.Experience;
import com.portfolio.app.model.ProfileSettings;
import com.portfolio.app.model.Project;
import com.portfolio.app.model.Skill;
import com.portfolio.app.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProjectService projectService;
    private final SkillService skillService;
    private final ContactService contactService;
    private final ProfileService profileService;
    private final BlogService blogService;
    private final ResumeService resumeService;
    private final CertificateService certificateService;
    private final ExperienceService experienceService;
    private final AdminUserService adminUserService;

    public AdminController(ProjectService projectService,
                           SkillService skillService,
                           ContactService contactService,
                           ProfileService profileService,
                           BlogService blogService,
                           ResumeService resumeService,
                           CertificateService certificateService,
                           ExperienceService experienceService,
                           AdminUserService adminUserService) {
        this.projectService = projectService;
        this.skillService = skillService;
        this.contactService = contactService;
        this.profileService = profileService;
        this.blogService = blogService;
        this.resumeService = resumeService;
        this.certificateService = certificateService;
        this.experienceService = experienceService;
        this.adminUserService = adminUserService;
    }

    @ModelAttribute
    public void addAdminAttributes(Model model, jakarta.servlet.http.HttpServletRequest request) {
        model.addAttribute("currentUri", request.getRequestURI());
        model.addAttribute("unreadMessages", contactService.getUnreadCount());
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully.");
        }
        return "admin/login";
    }

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("totalProjects", projectService.getTotalProjectsCount());
        model.addAttribute("totalSkills", skillService.getTotalSkillsCount());
        model.addAttribute("totalCertificates", certificateService.getTotalCertificatesCount());
        model.addAttribute("totalLikes", projectService.getTotalLikes());
        model.addAttribute("unreadMessages", contactService.getUnreadCount());
        model.addAttribute("totalVisits", profileService.getProfileSettings().getTotalVisits());
        model.addAttribute("recentMessages", contactService.getAllMessages().stream().limit(5).collect(Collectors.toList()));
        model.addAttribute("projects", projectService.getAllProjects().stream().limit(5).collect(Collectors.toList()));
        return "admin/dashboard";
    }

    // --- Projects Management ---

    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "admin/projects";
    }

    @GetMapping("/projects/new")
    public String newProjectForm(Model model) {
        Project project = new Project();
        model.addAttribute("project", project);
        model.addAttribute("techStackInput", "");
        return "admin/project-form";
    }

    @GetMapping("/projects/edit/{id}")
    public String editProjectForm(@PathVariable Long id, Model model) {
        return projectService.getProjectById(id)
                .map(project -> {
                    model.addAttribute("project", project);
                    String techStr = (project.getTechStack() != null)
                            ? String.join(", ", project.getTechStack())
                            : "";
                    model.addAttribute("techStackInput", techStr);
                    return "admin/project-form";
                })
                .orElse("redirect:/admin/projects");
    }

    @PostMapping("/projects/save")
    public String saveProject(@Valid @ModelAttribute("project") Project project,
                              BindingResult result,
                              @RequestParam(value = "techStackRaw", required = false) String techStackRaw,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("techStackInput", techStackRaw != null ? techStackRaw : "");
            return "admin/project-form";
        }

        if (techStackRaw != null && !techStackRaw.trim().isEmpty()) {
            List<String> list = Arrays.stream(techStackRaw.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            project.setTechStack(new ArrayList<>(list));
        } else {
            project.setTechStack(new ArrayList<>());
        }

        // Preserve created date, views, and likes if updating existing project
        if (project.getId() != null) {
            projectService.getProjectById(project.getId()).ifPresent(existing -> {
                if (project.getCreatedAt() == null) {
                    project.setCreatedAt(existing.getCreatedAt());
                }
                if (project.getLikesCount() == 0) {
                    project.setLikesCount(existing.getLikesCount());
                }
                if (project.getViewCount() == 0) {
                    project.setViewCount(existing.getViewCount());
                }
                if (project.getSlug() == null || project.getSlug().isEmpty()) {
                    project.setSlug(existing.getSlug());
                }
            });
        }

        projectService.saveProject(project);
        redirectAttributes.addFlashAttribute("successMessage", "Project saved successfully!");
        return "redirect:/admin/projects";
    }

    @PostMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteProject(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully!");
        return "redirect:/admin/projects";
    }

    // --- Skills Management ---

    @GetMapping("/skills")
    public String listSkills(Model model) {
        model.addAttribute("skills", skillService.getAllSkills());
        if (!model.containsAttribute("newSkill")) {
            model.addAttribute("newSkill", new Skill());
        }
        return "admin/skills";
    }

    @PostMapping("/skills/save")
    public String saveSkill(@Valid @ModelAttribute("newSkill") Skill skill,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("skills", skillService.getAllSkills());
            return "admin/skills";
        }
        skillService.saveSkill(skill);
        redirectAttributes.addFlashAttribute("successMessage", "Skill added successfully!");
        return "redirect:/admin/skills";
    }

    @PostMapping("/skills/delete/{id}")
    public String deleteSkill(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        skillService.deleteSkill(id);
        redirectAttributes.addFlashAttribute("successMessage", "Skill removed successfully!");
        return "redirect:/admin/skills";
    }

    // --- Messages Inbox ---

    @GetMapping("/messages")
    public String listMessages(Model model) {
        model.addAttribute("messages", contactService.getAllMessages());
        model.addAttribute("unreadCount", contactService.getUnreadCount());
        return "admin/messages";
    }

    @PostMapping("/messages/{id}/read")
    public String markMessageRead(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactService.markAsRead(id);
        redirectAttributes.addFlashAttribute("successMessage", "Message marked as read.");
        return "redirect:/admin/messages";
    }

    @PostMapping("/messages/{id}/delete")
    public String deleteMessage(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contactService.deleteMessage(id);
        redirectAttributes.addFlashAttribute("successMessage", "Message deleted successfully.");
        return "redirect:/admin/messages";
    }

    // --- Settings & Profile ---

    @GetMapping("/settings")
    public String settingsForm(Model model) {
        model.addAttribute("profileSettings", profileService.getProfileSettings());
        model.addAttribute("resumeDocument", resumeService.getLatestResume().orElse(null));
        model.addAttribute("experiences", experienceService.getAllExperiences());
        model.addAttribute("educations", experienceService.getAllEducations());
        model.addAttribute("adminUsername", adminUserService.getPrimaryAdminUsername());
        if (!model.containsAttribute("newExperience")) {
            model.addAttribute("newExperience", new Experience());
        }
        if (!model.containsAttribute("newEducation")) {
            model.addAttribute("newEducation", new Education());
        }
        return "admin/settings";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        String username = (principal != null) ? principal.getName() : adminUserService.getPrimaryAdminUsername();
        try {
            adminUserService.changePassword(username, currentPassword, newPassword, confirmPassword);
            redirectAttributes.addFlashAttribute("passwordSuccessMessage",
                    "Admin password changed successfully! Please remember your new password for your next login.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("passwordErrorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("passwordErrorMessage", "Failed to update password: " + e.getMessage());
        }
        return "redirect:/admin/settings#security-section";
    }

    @GetMapping("/resume")
    public String resumeSettingsRedirect() {
        return "redirect:/admin/settings#resume-section";
    }

    @PostMapping("/settings/save")
    public String saveSettings(@ModelAttribute("profileSettings") ProfileSettings settings,
                               RedirectAttributes redirectAttributes) {
        profileService.updateProfileSettings(settings);
        redirectAttributes.addFlashAttribute("successMessage", "Profile & site settings updated successfully!");
        return "redirect:/admin/settings";
    }

    @PostMapping("/resume/upload")
    public String uploadResume(@RequestParam("resumeFile") org.springframework.web.multipart.MultipartFile resumeFile,
                               RedirectAttributes redirectAttributes) {
        try {
            com.portfolio.app.model.ResumeDocument saved = resumeService.saveOrUpdateResume(resumeFile);
            redirectAttributes.addFlashAttribute("resumeSuccessMessage",
                    "Resume PDF ('" + saved.getFileName() + "') uploaded and replaced successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("resumeErrorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("resumeErrorMessage",
                    "Failed to upload resume PDF: " + e.getMessage());
        }
        return "redirect:/admin/settings#resume-section";
    }

    // --- Experience Management ---

    @PostMapping("/experience/save")
    public String saveExperience(@Valid @ModelAttribute("newExperience") Experience experience,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("experienceErrorMessage", "Please fill in role and company.");
            return "redirect:/admin/settings#experience-section";
        }
        experienceService.saveExperience(experience);
        redirectAttributes.addFlashAttribute("experienceSuccessMessage", "Work experience saved successfully!");
        return "redirect:/admin/settings#experience-section";
    }

    @PostMapping("/experience/delete/{id}")
    public String deleteExperience(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        experienceService.deleteExperience(id);
        redirectAttributes.addFlashAttribute("experienceSuccessMessage", "Work experience removed successfully!");
        return "redirect:/admin/settings#experience-section";
    }

    // --- Education Management ---

    @PostMapping("/education/save")
    public String saveEducation(@ModelAttribute("newEducation") Education education,
                                RedirectAttributes redirectAttributes) {
        experienceService.saveEducation(education);
        redirectAttributes.addFlashAttribute("educationSuccessMessage", "Education entry saved successfully!");
        return "redirect:/admin/settings#education-section";
    }

    @PostMapping("/education/delete/{id}")
    public String deleteEducation(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        experienceService.deleteEducation(id);
        redirectAttributes.addFlashAttribute("educationSuccessMessage", "Education entry removed successfully!");
        return "redirect:/admin/settings#education-section";
    }

    // --- Certificates Management ---

    @GetMapping("/certificates")
    public String listCertificates(Model model) {
        model.addAttribute("certificates", certificateService.getAllCertificates());
        if (!model.containsAttribute("newCertificate")) {
            model.addAttribute("newCertificate", new Certificate());
        }
        return "admin/certificates";
    }

    @PostMapping("/certificates/save")
    public String saveCertificate(@Valid @ModelAttribute("newCertificate") Certificate certificate,
                                  BindingResult result,
                                  @RequestParam(value = "certificateFile", required = false) org.springframework.web.multipart.MultipartFile certificateFile,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("certificates", certificateService.getAllCertificates());
            return "admin/certificates";
        }

        try {
            certificateService.saveCertificate(certificate, certificateFile);
            redirectAttributes.addFlashAttribute("successMessage", "Certificate '" + certificate.getTitle() + "' saved successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload certificate: " + e.getMessage());
        }

        return "redirect:/admin/certificates";
    }

    @PostMapping("/certificates/delete/{id}")
    public String deleteCertificate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        certificateService.deleteCertificate(id);
        redirectAttributes.addFlashAttribute("successMessage", "Certificate deleted successfully!");
        return "redirect:/admin/certificates";
    }
}
