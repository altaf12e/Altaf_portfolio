package com.portfolio.app.config;

import com.portfolio.app.model.*;
import com.portfolio.app.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(DataInitializer.class);

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;
    private final TestimonialRepository testimonialRepository;
    private final BlogPostRepository blogPostRepository;
    private final ContactMessageRepository contactMessageRepository;
    private final ProfileSettingsRepository profileSettingsRepository;
    private final com.portfolio.app.service.ResumeService resumeService;
    private final com.portfolio.app.service.CertificateService certificateService;
    private final com.portfolio.app.service.AdminUserService adminUserService;

    public DataInitializer(
            ProjectRepository projectRepository,
            SkillRepository skillRepository,
            ExperienceRepository experienceRepository,
            EducationRepository educationRepository,
            TestimonialRepository testimonialRepository,
            BlogPostRepository blogPostRepository,
            ContactMessageRepository contactMessageRepository,
            ProfileSettingsRepository profileSettingsRepository,
            com.portfolio.app.service.ResumeService resumeService,
            com.portfolio.app.service.CertificateService certificateService,
            com.portfolio.app.service.AdminUserService adminUserService) {

        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
        this.testimonialRepository = testimonialRepository;
        this.blogPostRepository = blogPostRepository;
        this.contactMessageRepository = contactMessageRepository;
        this.profileSettingsRepository = profileSettingsRepository;
        this.resumeService = resumeService;
        this.certificateService = certificateService;
        this.adminUserService = adminUserService;
    }

    @Override
    public void run(String... args) {

        adminUserService.initDefaultAdminIfNotPresent();
        initProfile();
        initSkills();
        initProjects();
        initExperiences();
        initEducation();
        initTestimonials();
        initBlogPosts();
        initContactMessages();
        resumeService.initDefaultResumeIfNotPresent();
        certificateService.initDefaultCertificatesIfNotPresent();

        log.info("Portfolio initial database seed completed successfully.");
    }

    private void initProfile() {

        if (profileSettingsRepository.count() == 0) {

            ProfileSettings profile = new ProfileSettings();

            profile.setFullName("Altaf Hussain");
            profile.setTitle("Senior Full Stack & Java Engineer");
            profile.setTagline(
                    "Architecting resilient distributed microservices and modern responsive web systems with Spring Boot & Cloud technologies."
            );

            profile.setAboutText(
                    "I am a passionate software engineer with over 5 years of hands-on experience designing and delivering high-throughput, mission-critical backend systems and modern full-stack web applications. My core toolkit centers on Java 17/21, Spring Boot, Spring Cloud, Kafka, Docker, Kubernetes, and relational/NoSQL datastores. I believe in clean code, automated test suites, domain-driven design, and pragmatic problem-solving."
            );

            profile.setEmail("altaf.engineer@example.com");
            profile.setPhone("+1 (555) 349-8201");
            profile.setLocation("San Francisco, CA (Open to Worldwide Remote)");
            profile.setGithubUrl("https://github.com");
            profile.setLinkedinUrl("https://linkedin.com");
            profile.setTwitterUrl("https://twitter.com");
            profile.setEmail("altafhussain078692@gmail.com");
            profile.setPhone("+91-8002590676");
            profile.setLocation("Lucknow, India");
            profile.setGithubUrl("https://github.com/altaf12e");
            profile.setLinkedinUrl("https://www.linkedin.com/in/altaf-hussain-75b9ab329/");
            profile.setTwitterUrl("");
            profile.setResumeUrl("/resume");
            profile.setAvatarUrl("/images/altaf.jpeg");
            profile.setAvailableForHire(true);
            profile.setTotalVisits(1420);

            profileSettingsRepository.save(profile);
        } else {
            ProfileSettings profile = profileSettingsRepository.findAll().get(0);
            if (profile.getEmail() == null || profile.getEmail().contains("example.com") || profile.getEmail().contains("altaf.engineer")) {
                profile.setEmail("altafhussain078692@gmail.com");
                profileSettingsRepository.save(profile);
            }
        }
    }

    private void initSkills() {

        if (skillRepository.count() == 0) {

            skillRepository.saveAll(Arrays.asList(

                    // Backend
                    new Skill(
                            "Java 17 / 21",
                            95,
                            "Backend",
                            "fa-brands fa-java",
                            5,
                            true
                    ),

                    new Skill(
                            "Spring Boot 3",
                            92,
                            "Backend",
                            "fa-solid fa-leaf",
                            5,
                            true
                    ),

                    new Skill(
                            "Spring Cloud & Microservices",
                            88,
                            "Backend",
                            "fa-solid fa-cloud",
                            4,
                            true
                    ),

                    new Skill(
                            "RESTful & GraphQL APIs",
                            90,
                            "Backend",
                            "fa-solid fa-network-wired",
                            5,
                            true
                    ),

                    new Skill(
                            "Kafka & Event Streaming",
                            82,
                            "Backend",
                            "fa-solid fa-bolt",
                            3,
                            false
                    ),

                    // Database
                    new Skill(
                            "PostgreSQL",
                            88,
                            "Database",
                            "fa-solid fa-database",
                            4,
                            true
                    ),

                    new Skill(
                            "MySQL",
                            85,
                            "Database",
                            "fa-solid fa-server",
                            4,
                            false
                    ),

                    new Skill(
                            "Redis & Distributed Caching",
                            80,
                            "Database",
                            "fa-solid fa-memory",
                            3,
                            false
                    ),

                    // DevOps & Cloud
                    new Skill(
                            "Docker & Containers",
                            88,
                            "DevOps & Cloud",
                            "fa-brands fa-docker",
                            4,
                            true
                    ),

                    new Skill(
                            "Kubernetes",
                            78,
                            "DevOps & Cloud",
                            "fa-solid fa-cubes",
                            2,
                            false
                    ),

                    new Skill(
                            "AWS (ECS, S3, RDS, Lambda)",
                            84,
                            "DevOps & Cloud",
                            "fa-brands fa-aws",
                            3,
                            true
                    ),

                    new Skill(
                            "CI/CD (GitHub Actions)",
                            85,
                            "DevOps & Cloud",
                            "fa-solid fa-arrows-split-up-and-left",
                            3,
                            false
                    ),

                    // Frontend
                    new Skill(
                            "JavaScript (ES6+) & TypeScript",
                            82,
                            "Frontend",
                            "fa-brands fa-js",
                            4,
                            true
                    ),

                    new Skill(
                            "HTML5 / CSS3 / Modern Flexbox & Grid",
                            88,
                            "Frontend",
                            "fa-brands fa-html5",
                            5,
                            true
                    ),

                    new Skill(
                            "Thymeleaf & Server Templating",
                            90,
                            "Frontend",
                            "fa-solid fa-code",
                            4,
                            false
                    ),

                    // Core Tools
                    new Skill(
                            "Git & GitHub Collaboration",
                            92,
                            "Core Tools",
                            "fa-brands fa-git-alt",
                            5,
                            true
                    ),

                    new Skill(
                            "JUnit 5, Mockito & Testcontainers",
                            89,
                            "Core Tools",
                            "fa-solid fa-vial-circle-check",
                            4,
                            true
                    ),

                    new Skill(
                            "Linux & Shell Scripting",
                            85,
                            "Core Tools",
                            "fa-brands fa-linux",
                            4,
                            false
                    )
            ));
        }
    }

    private void initProjects() {
        boolean needsUpdate = false;
        if (projectRepository.count() == 0) {
            needsUpdate = true;
        } else {
            java.util.List<Project> current = projectRepository.findAll();
            for (Project p : current) {
                if (p.getTitle() != null && (p.getTitle().contains("Cloud Task")
                        || p.getTitle().contains("High-Throughput E-Commerce API")
                        || p.getTitle().contains("DevOps Observability")
                        || p.getTitle().contains("Real-Time Collaboration")
                        || p.getTitle().contains("Full-Stack Portfolio")
                        || p.getTitle().contains("AI Resume Matcher"))) {
                    needsUpdate = true;
                    break;
                }
            }
            if (current.size() != 5) {
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            projectRepository.deleteAll();

            Project p1 = new Project(
                    "E-Commerce App",
                    "Full-Stack Shopping Application",
                    "A full-stack shopping app with cart, authentication, and payment features.",
                    "Fullstack",
                    Arrays.asList("HTML", "CSS", "JavaScript", "Node.js", "MongoDB"),
                    "https://github.com/altaf12e/E-commerce",
                    "https://github.com/altaf12e/E-commerce",
                    "https://images.unsplash.com/photo-1556742049-0a67e55722c0?auto=format&fit=crop&w=800&q=80",
                    true
            );
            p1.setLikesCount(12);
            p1.setViewCount(140);

            Project p2 = new Project(
                    "ViastaStore-ECommerce Website",
                    "Spring Boot & MySQL E-Commerce Platform",
                    "ViastaStore is a full-stack e-commerce website built with Java Spring Boot, MySQL, Thymeleaf, HTML, CSS, and JavaScript, featuring product browsing, cart, wishlist, authentication, orders, and admin management.",
                    "Fullstack",
                    Arrays.asList("Java", "SpringBoot", "MySQL", "Thymeleaf", "HTML", "CSS", "JavaScript"),
                    "https://github.com/altaf12e/ViastaStore-Ecommerce-Website",
                    "https://github.com/altaf12e/ViastaStore-Ecommerce-Website",
                    "https://images.unsplash.com/photo-1472851294608-062f824d29cc?auto=format&fit=crop&w=800&q=80",
                    true
            );
            p2.setLikesCount(28);
            p2.setViewCount(290);

            Project p3 = new Project(
                    "Quiz App",
                    "Interactive Quiz Application",
                    "Interactive quiz app with timer, score tracking, and multiple categories.",
                    "Frontend",
                    Arrays.asList("HTML", "CSS", "JavaScript", "Express.js"),
                    "https://github.com/altaf12e/CodeQuest-QUIZ-BASED-WEBSITE",
                    "https://github.com/altaf12e/CodeQuest-QUIZ-BASED-WEBSITE",
                    "https://images.unsplash.com/photo-1606326608606-aa0b62935f2b?auto=format&fit=crop&w=800&q=80",
                    true
            );
            p3.setLikesCount(19);
            p3.setViewCount(210);

            Project p4 = new Project(
                    "Social Media App",
                    "Interactive Social Network Platform",
                    "Sleek social media app with secure messaging, personalized feeds, and multilingual support.",
                    "Fullstack",
                    Arrays.asList("HTML", "CSS", "JavaScript", "Node.js", "MongoDB"),
                    "https://github.com/altaf12e/Social-Media-App",
                    "https://github.com/altaf12e/Social-Media-App",
                    "https://images.unsplash.com/photo-1611162617474-5b21e879e113?auto=format&fit=crop&w=800&q=80",
                    true
            );
            p4.setLikesCount(24);
            p4.setViewCount(260);

            Project p5 = new Project(
                    "Portfolio Website",
                    "Modern Portfolio & Interactive Showcase",
                    "A personal portfolio website with dark/light mode, particle background, typed animations, skill bars, timeline, and project filters.",
                    "Frontend",
                    Arrays.asList("HTML", "CSS", "JavaScript", "SpringBoot", "Thymeleaf"),
                    "https://github.com/altaf12e/Altaf_portfolio",
                    "https://altaf-ten.vercel.app/",
                    "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=800&q=80",
                    true
            );
            p5.setLikesCount(35);
            p5.setViewCount(380);

            projectRepository.save(p1);
            projectRepository.save(p2);
            projectRepository.save(p3);
            projectRepository.save(p4);
            projectRepository.save(p5);

            log.info("Vercel-matched real projects initialized successfully (5 records).");
        }
    }

    private void initExperiences() {
        boolean needsUpdate = false;
        if (experienceRepository.count() == 0) {
            needsUpdate = true;
        } else {
            java.util.List<Experience> current = experienceRepository.findAll();
            for (Experience e : current) {
                if (e.getCompany() != null && (e.getCompany().contains("Apex Cloud Solutions")
                        || e.getCompany().contains("NextGen Fintech")
                        || e.getCompany().contains("DataWave Technologies"))) {
                    needsUpdate = true;
                    break;
                }
            }
        }

        if (needsUpdate) {
            experienceRepository.deleteAll();

            experienceRepository.saveAll(Arrays.asList(
                    new Experience(
                            "Java Spring Boot Training",
                            "Techpile Technology Pvt. Ltd., Lucknow",
                            "https://www.techpile.in",
                            "Lucknow, India",
                            "2026",
                            "Successfully completed a 45-day intensive Summer Training in Java Spring Boot at Techpile Technology Pvt. Ltd., Lucknow, receiving an A++ Grade.",
                            "Java, Spring Boot, REST APIs, MVC Architecture, MySQL, Git",
                            1
                    ),
                    new Experience(
                            "Project Development (ViastaStore)",
                            "Self-Driven & Academic Project",
                            "https://github.com/altaf12e/ViastaStore-Ecommerce-Website",
                            "Lucknow, India",
                            "2026",
                            "Worked on practical projects such as ViastaStore, gaining hands-on experience in backend development, database integration, authentication, and responsive UI.",
                            "Java, Spring Boot, MySQL, Thymeleaf, HTML5, CSS3, JavaScript",
                            2
                    ),
                    new Experience(
                            "Full-Stack Web Development",
                            "Personal & Open Source Projects",
                            "https://github.com/altaf12e",
                            "Lucknow, India",
                            "2025",
                            "Focused on full-stack web development and began working with Java, Spring Boot, MySQL, Thymeleaf, and modern web technologies.",
                            "Java, Spring Boot, Node.js, Express.js, MongoDB, JavaScript",
                            3
                    ),
                    new Experience(
                            "Programming Foundations",
                            "Academic & Skill Building",
                            "https://github.com/altaf12e",
                            "Lucknow, India",
                            "2024",
                            "Learned and practiced Java, HTML, CSS, JavaScript, and database concepts through academics and personal projects.",
                            "Java, Data Structures, OOP, SQL, HTML, CSS, JavaScript",
                            4
                    ),
                    new Experience(
                            "Started B.Tech Computer Science",
                            "Dr. A.P.J. Abdul Kalam Technical University",
                            "https://aktu.ac.in",
                            "Lucknow, India",
                            "2023",
                            "Started Bachelor of Technology in Computer Science and began building a strong foundation in programming and computer science.",
                            "Computer Science, Programming Fundamentals, Algorithms",
                            5
                    )
            ));
            log.info("Vercel-matched timeline experiences initialized successfully.");
        }
    }

    private void initEducation() {
        boolean needsUpdate = false;
        if (educationRepository.count() == 0) {
            needsUpdate = true;
        } else {
            java.util.List<Education> current = educationRepository.findAll();
            for (Education ed : current) {
                if (ed.getInstitution() != null && (ed.getInstitution().contains("University of California")
                        || ed.getDegree().contains("AWS Certified Solutions"))) {
                    needsUpdate = true;
                    break;
                }
            }
        }

        if (needsUpdate) {
            educationRepository.deleteAll();

            educationRepository.saveAll(Arrays.asList(
                    new Education(
                            "Bachelor of Technology in Computer Science",
                            "Dr. A.P.J. Abdul Kalam Technical University, Lucknow",
                            "2023 - 2027",
                            "Pursuing B.Tech in Computer Science & Engineering. SGPA: 8.0/10. Focus on Software Engineering, Data Structures, Algorithms, and Full-Stack Web Development.",
                            1
                    ),
                    new Education(
                            "Class 12th (Senior Secondary)",
                            "Bihar School Examination Board, Patna (Bihar)",
                            "2022",
                            "Completed Senior Secondary Education with Science stream (63.4%).",
                            2
                    )
            ));
            log.info("Real education records initialized successfully.");
        }
    }

    private void initTestimonials() {

        if (testimonialRepository.count() == 0) {

            testimonialRepository.saveAll(Arrays.asList(

                    new Testimonial(
                            "Sarah Jenkins",
                            "Director of Engineering",
                            "Apex Cloud Solutions",
                            "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=160&q=80",
                            "Altaf is one of the most reliable and technically sound engineers I have worked with. His mastery over Spring Boot and scalable cloud architectures helped us launch our flagship platform on time without a hitch.",
                            5
                    ),

                    new Testimonial(
                            "David Zhao",
                            "Lead Software Architect",
                            "NextGen Fintech Systems",
                            "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=160&q=80",
                            "A true backend expert who writes remarkably clean, self-documenting code. Altaf restructured our caching layer and reduced latency drastically. Highly recommended!",
                            5
                    ),

                    new Testimonial(
                            "Elena Rostova",
                            "Principal Product Manager",
                            "Enterprise Data Labs",
                            "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=160&q=80",
                            "What sets Altaf apart is his exceptional product intuition combined with deep engineering rigor. He doesn't just build APIs—he solves real customer problems.",
                            5
                    )
            ));
        }
    }

    private void initBlogPosts() {

        if (blogPostRepository.count() == 0) {

            blogPostRepository.saveAll(Arrays.asList(

                    new BlogPost(
                            "Demystifying Spring Boot 3 & Virtual Threads in Java 21",
                            "How Project Loom and Virtual Threads revolutionize high-throughput I/O bound web applications in Spring Boot 3.",
                            "With Java 21 LTS and Spring Boot 3.2+, virtual threads (Project Loom) have fundamentally changed how we think about concurrent request handling in Java applications.\n\nTraditionally, the Spring MVC servlet container allocated one heavy OS platform thread per client request. Under high load with blocking I/O calls—such as database queries or external REST calls—thousands of idle threads consumed gigabytes of heap space and spent cycles on context switching.\n\nVirtual threads are lightweight user-mode threads managed directly by the Java Virtual Machine. By adding a single configuration property (`spring.threads.virtual.enabled=true`), Tomcat dispatches incoming HTTP requests onto virtual threads. When a virtual thread encounters blocking I/O, the JVM unmounts it from its carrier thread and lets other tasks run—yielding massive throughput improvements with zero changes to existing imperative code.\n\nIn this article, we benchmark a Spring Boot 3 application under 10,000 concurrent connections and explore the best practices for thread locals, database connection pools, and synchronized blocks.",
                            "Java 21, Spring Boot 3, Virtual Threads, Performance",
                            7
                    ),

                    new BlogPost(
                            "Architecting Event-Driven Microservices with Spring Boot & Apache Kafka",
                            "A practical guide to implementing the Transactional Outbox pattern and guaranteed delivery in distributed systems.",
                            "Building distributed microservices that remain eventually consistent during network partitions is one of the most critical challenges in software engineering today.\n\nA common anti-pattern is saving a business entity to the database and immediately attempting to publish a Kafka event within the same REST controller method. If Kafka is temporarily unreachable, or if the server crashes right after saving, data consistency is compromised.\n\nEnter the **Transactional Outbox Pattern**: By inserting the outbound event into a dedicated `outbox` table within the exact same database transaction as your business entity, you guarantee atomic commit semantics. A background CDC worker or Debezium poller then reads outbox entries and publishes them to Kafka with at-least-once delivery guarantees.\n\nLet's walk through building an idempotent consumer in Spring Boot using `@KafkaListener` and consumer acknowledgements.",
                            "Spring Boot, Apache Kafka, Microservices, Architecture",
                            9
                    ),

                    new BlogPost(
                            "Writing Clean, Resilient REST APIs: Principles, Error Handling & Observability",
                            "From RFC 7807 Problem Details to structured logging and rate limiting, here is how to design enterprise APIs that developers love.",
                            "Great APIs are not just functional; they are predictable, intuitive, and resilient.\n\nKey pillars of modern API design include:\n1. **Consistent Error Contracts**: Leveraging RFC 7807 `ProblemDetail` introduced in Spring 6 to provide actionable, machine-readable HTTP error payloads.\n2. **Defensive Validation**: Applying Jakarta Bean Validation (`@Valid`, `@NotNull`, `@Size`) at the boundary to catch bad requests early.\n3. **Idempotency**: Using idempotency keys for non-safe HTTP methods (POST, PATCH) to prevent accidental duplicate charges or duplicate writes.\n4. **Structured Observability**: Emitting Micrometer metrics, OpenTelemetry traces, and correlated MDC transaction IDs for painless debugging across microservices.",
                            "REST API, Best Practices, Clean Code, Spring 6",
                            6
                    )
            ));
        }
    }

    private void initContactMessages() {

        if (contactMessageRepository.count() == 0) {

            ContactMessage m1 = new ContactMessage(
                    "Sarah Miller",
                    "sarah.miller@innovatetech.io",
                    "Senior Backend Role Opportunity",
                    "Hi Altaf, I came across your portfolio and was very impressed by your distributed systems projects. We have an open Senior Backend Engineer position on our cloud platform team and would love to connect."
            );

            m1.setCreatedAt(LocalDateTime.now().minusHours(4));
            m1.setReadStatus(false);

            ContactMessage m2 = new ContactMessage(
                    "Marcus Vance",
                    "marcus@fintechnexus.com",
                    "Consulting on Kafka Migration",
                    "Hello! We are currently designing a financial data streaming service and need expert consulting on Kafka and Spring Boot event pipelines. Are you available for contract consulting?"
            );

            m2.setCreatedAt(LocalDateTime.now().minusDays(1));
            m2.setReadStatus(true);
            m2.setReplied(true);

            contactMessageRepository.saveAll(Arrays.asList(m1, m2));
        }
    }
}