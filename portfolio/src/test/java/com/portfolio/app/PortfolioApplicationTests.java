package com.portfolio.app;

import com.portfolio.app.model.ContactMessage;
import com.portfolio.app.repository.ContactMessageRepository;
import com.portfolio.app.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PortfolioApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactService contactService;

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @Test
    void contextLoads() {
        assertNotNull(contactService);
    }

    @Test
    void testSaveContactMessageAppearsInRepository() {
        long initialCount = contactMessageRepository.count();

        ContactMessage msg = new ContactMessage(
                "Test User",
                "test@example.com",
                "New Project Inquiry",
                "Hello, I want to discuss a software project."
        );
        ContactMessage saved = contactService.saveMessage(msg);

        assertNotNull(saved.getId());
        assertEquals(initialCount + 1, contactMessageRepository.count());
        assertFalse(saved.isReadStatus());
        assertEquals("Test User", saved.getName());
    }

    @Test
    void testContactApiJsonSubmission() throws Exception {
        long initialCount = contactMessageRepository.count();

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "Alice Developer",
                        "email": "alice@example.com",
                        "subject": "Freelance Project",
                        "message": "Interested in building a Spring Boot backend."
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        assertEquals(initialCount + 1, contactMessageRepository.count());
    }

    @Test
    void testContactApiFormDataSubmission() throws Exception {
        long initialCount = contactMessageRepository.count();

        mockMvc.perform(post("/api/v1/contact")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Bob Recruiter")
                .param("email", "bob@example.com")
                .param("subject", "Hiring Inquiry")
                .param("message", "We have an open Senior Java role."))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        assertEquals(initialCount + 1, contactMessageRepository.count());
    }

    @Test
    void testContactStandardFormPost() throws Exception {
        long initialCount = contactMessageRepository.count();

        mockMvc.perform(post("/contact")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "Charlie Client")
                .param("email", "charlie@example.com")
                .param("subject", "Website Project")
                .param("message", "Need an enterprise portfolio and backend built."))
                .andExpect(status().is3xxRedirection());

        assertEquals(initialCount + 1, contactMessageRepository.count());
        assertTrue(contactService.getUnreadCount() > 0);
    }

    @Autowired
    private com.portfolio.app.service.ResumeService resumeService;

    @Test
    void testViewResumeEndpointReturnsPdf() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resume/view"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.hamcrest.Matchers.containsString("inline; filename=")));
    }

    @Test
    void testDownloadResumeEndpointReturnsPdfAttachment() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resume/download"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.hamcrest.Matchers.containsString("attachment; filename=")));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminUploadResumePdfSuccess() throws Exception {
        org.springframework.mock.web.MockMultipartFile validPdf = new org.springframework.mock.web.MockMultipartFile(
                "resumeFile",
                "My_Custom_Resume.pdf",
                "application/pdf",
                "%PDF-1.4 custom test resume content %%EOF".getBytes()
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/admin/resume/upload")
                        .file(validPdf)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash().attributeExists("resumeSuccessMessage"));

        assertTrue(resumeService.getLatestResume().isPresent());
        assertEquals("My_Custom_Resume.pdf", resumeService.getLatestResume().get().getFileName());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminUploadNonPdfRejected() throws Exception {
        org.springframework.mock.web.MockMultipartFile invalidFile = new org.springframework.mock.web.MockMultipartFile(
                "resumeFile",
                "not_a_pdf.txt",
                "text/plain",
                "Just plain text".getBytes()
        );

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/admin/resume/upload")
                        .file(invalidFile)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash().attributeExists("resumeErrorMessage"));
    }

    @Test
    void testAllPublicPagesReturnOk() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/about"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/projects"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/certificates"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/resume"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/blog"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/contact"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/admin/login"))
                .andExpect(status().isOk());
    }

    @Test
    void testAdminUnauthenticatedRedirectsToLogin() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/admin/dashboard"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminAuthenticatedAccessDashboard() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/admin/dashboard"))
                .andExpect(status().isOk());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/admin/settings"))
                .andExpect(status().isOk());
    }

    @Test
    void testApiStatsAndSkills() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalProjects").isNumber());

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/v1/skills"))
                .andExpect(status().isOk());
    }

    @Autowired
    private com.portfolio.app.service.CertificateService certificateService;

    @Test
    void testViewAndDownloadCertificateEndpoints() throws Exception {
        java.util.List<com.portfolio.app.model.Certificate> certs = certificateService.getAllCertificates();
        assertFalse(certs.isEmpty(), "Certificates should be initialized by seed");
        com.portfolio.app.model.Certificate sample = certs.get(0);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/certificates/view/" + sample.getId()))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.hamcrest.Matchers.containsString("inline; filename=")));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/certificates/download/" + sample.getId()))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.header().string(
                        org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.hamcrest.Matchers.containsString("attachment; filename=")));
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminUploadCertificatePdfSuccess() throws Exception {
        org.springframework.mock.web.MockMultipartFile samplePdf = new org.springframework.mock.web.MockMultipartFile(
                "certificateFile",
                "Kubernetes_CKA.pdf",
                "application/pdf",
                "%PDF-1.4 test cka certificate %%EOF".getBytes()
        );

        long initialCount = certificateService.getTotalCertificatesCount();

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/admin/certificates/save")
                        .file(samplePdf)
                        .param("title", "Certified Kubernetes Administrator")
                        .param("issuingOrganization", "CNCF / Linux Foundation")
                        .param("issueDate", "July 2024")
                        .param("credentialId", "CKA-102948")
                        .param("description", "Kubernetes cluster administration, networking, security")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash().attributeExists("successMessage"));

        assertEquals(initialCount + 1, certificateService.getTotalCertificatesCount());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminUploadCertificateImageSuccess() throws Exception {
        org.springframework.mock.web.MockMultipartFile sampleImg = new org.springframework.mock.web.MockMultipartFile(
                "certificateFile",
                "Docker_Captain.png",
                "image/png",
                "fake-png-bytes".getBytes()
        );

        long initialCount = certificateService.getTotalCertificatesCount();

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart("/admin/certificates/save")
                        .file(sampleImg)
                        .param("title", "Docker Certified Associate")
                        .param("issuingOrganization", "Docker Inc.")
                        .param("issueDate", "March 2024")
                        .param("credentialId", "DCA-783921")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash().attributeExists("successMessage"));

        assertEquals(initialCount + 1, certificateService.getTotalCertificatesCount());
    }

    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = {"ADMIN"})
    void testAdminDeleteCertificate() throws Exception {
        com.portfolio.app.model.Certificate temp = new com.portfolio.app.model.Certificate(
                "Temporary Cert to Delete",
                "Test Org",
                "Jan 2024",
                "TEMP-1",
                null,
                null,
                "temp.pdf",
                "application/pdf",
                100L,
                "dummy".getBytes()
        );
        com.portfolio.app.model.Certificate saved = certificateService.saveCertificate(temp, null);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/admin/certificates/delete/" + saved.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash().attributeExists("successMessage"));

        assertTrue(certificateService.getCertificateById(saved.getId()).isEmpty());
    }
}

