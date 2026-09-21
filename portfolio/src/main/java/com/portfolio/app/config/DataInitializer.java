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
            com.portfolio.app.service.CertificateService certificateService) {

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
    }

    @Override
    public void run(String... args) {

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
            profile.setResumeUrl("/resume");
            profile.setAvatarUrl("/images/altaf.jpeg");
            profile.setAvailableForHire(true);
            profile.setTotalVisits(1420);

            profileSettingsRepository.save(profile);
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

        if (projectRepository.count() == 0) {

            Project p1 = new Project(
                    "Cloud Task & Workflow Microservices",
                    "Distributed enterprise task orchestration engine",
                    "A fault-tolerant microservice architecture for workflow scheduling, event processing, and distributed task execution built with Spring Boot 3, Spring Cloud Gateway, Apache Kafka, and PostgreSQL.",
                    "Backend",
                    Arrays.asList(
                            "Java 21",
                            "Spring Boot 3",
                            "Spring Cloud",
                            "Kafka",
                            "PostgreSQL",
                            "Docker"
                    ),
                    "https://github.com",
                    "https://example.com/demo/tasks",
                    "https://images.unsplash.com/photo-1551288049-bebda4e38f71?auto=format&fit=crop&w=800&q=80",
                    true
            );

            p1.setLikesCount(48);
            p1.setViewCount(320);

            Project p2 = new Project(
                    "High-Throughput E-Commerce API",
                    "Real-time inventory and checkout processing pipeline",
                    "Event-driven order management system with distributed transaction coordination using Saga pattern, Redis multi-level caching, Stripe webhook integration, and resilient circuit breakers via Resilience4j.",
                    "Backend",
                    Arrays.asList(
                            "Java 17",
                            "Spring Boot",
                            "Redis",
                            "Resilience4j",
                            "MySQL",
                            "Docker"
                    ),
                    "https://github.com",
                    "https://example.com/demo/shop",
                    "https://images.unsplash.com/photo-1556742049-0a67e55722c0?auto=format&fit=crop&w=800&q=80",
                    true
            );

            p2.setLikesCount(35);
            p2.setViewCount(240);

            Project p3 = new Project(
                    "Full-Stack Portfolio & CMS Platform",
                    "Modern Spring Boot showcase with interactive dashboard",
                    "Production-ready portfolio web application featuring dynamic H2/MySQL persistence, secured role-based Admin CMS, live project search and category filtering, printable resume, and visitor analytics.",
                    "Fullstack",
                    Arrays.asList(
                            "Spring Boot 3",
                            "Thymeleaf",
                            "Spring Security",
                            "JPA",
                            "Vanilla JS",
                            "CSS3"
                    ),
                    "https://github.com",
                    "#",
                    "https://images.unsplash.com/photo-1460925895917-afdab827c52f?auto=format&fit=crop&w=800&q=80",
                    true
            );

            p3.setLikesCount(62);
            p3.setViewCount(510);

            Project p4 = new Project(
                    "DevOps Observability & Health Portal",
                    "Real-time container metrics & automated alert system",
                    "Centralized monitoring dashboard aggregating Prometheus metrics, Spring Boot Actuator endpoints, and Docker container health checks with customizable Slack webhook notification triggers.",
                    "Cloud / DevOps",
                    Arrays.asList(
                            "Spring Boot",
                            "Prometheus",
                            "Grafana",
                            "Docker",
                            "Kubernetes",
                            "AWS"
                    ),
                    "https://github.com",
                    "https://example.com/demo/devops",
                    "https://images.unsplash.com/photo-1504639725590-34d0984388bd?auto=format&fit=crop&w=800&q=80",
                    false
            );

            p4.setLikesCount(29);
            p4.setViewCount(190);

            Project p5 = new Project(
                    "AI Resume Matcher & Skill Extractor",
                    "NLP-powered candidate job fit scoring engine",
                    "Intelligent document parsing application analyzing PDF resumes against job requirements, extracting key skills, and generating semantic similarity scores with OpenAI API and Spring AI.",
                    "AI / Tools",
                    Arrays.asList(
                            "Spring AI",
                            "Java",
                            "OpenAI API",
                            "PostgreSQL",
                            "Apache PDFBox"
                    ),
                    "https://github.com",
                    "https://example.com/demo/resume-ai",
                    "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?auto=format&fit=crop&w=800&q=80",
                    true
            );

            p5.setLikesCount(54);
            p5.setViewCount(430);

            Project p6 = new Project(
                    "Real-Time Collaboration & Chat Engine",
                    "WebSocket-based multi-room messaging platform",
                    "Low-latency communication server supporting WebSocket STOMP protocol, message persistence with MongoDB, presence tracking, and end-to-end message acknowledgement guarantees.",
                    "Fullstack",
                    Arrays.asList(
                            "Spring WebSockets",
                            "STOMP",
                            "SockJS",
                            "Redis Pub/Sub",
                            "Bootstrap"
                    ),
                    "https://github.com",
                    "https://example.com/demo/chat",
                    "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=800&q=80",
                    false
            );

            p6.setLikesCount(22);
            p6.setViewCount(165);

            /*
             * Save projects one by one.
             * This ensures that each parent Project is saved
             * before Hibernate inserts its technologies.
             */
            projectRepository.save(p1);
            projectRepository.save(p2);
            projectRepository.save(p3);
            projectRepository.save(p4);
            projectRepository.save(p5);
            projectRepository.save(p6);

            log.info("Projects initialized successfully.");
        }
    }

    private void initExperiences() {

        if (experienceRepository.count() == 0) {

            experienceRepository.saveAll(Arrays.asList(

                    new Experience(
                            "Senior Backend Engineer",
                            "Apex Cloud Solutions",
                            "https://example.com",
                            "San Francisco, CA",
                            "2023 - Present",
                            "Leading the design and development of core microservices handling over 50M daily API requests. Migrated monolithic services to containerized Spring Boot microservices on AWS EKS, improving deployment cycle time by 60%. Mentored junior engineers and instituted automated integration testing with Testcontainers.",
                            "Java 21, Spring Boot 3, Kafka, Docker, Kubernetes, AWS EKS, PostgreSQL",
                            1
                    ),

                    new Experience(
                            "Software Development Engineer",
                            "NextGen Fintech Systems",
                            "https://example.com",
                            "Austin, TX",
                            "2021 - 2023",
                            "Engineered transaction reconciliation pipelines and RESTful banking integrations. Designed Redis caching layers that reduced p99 query latency from 320ms to 45ms. Built comprehensive JUnit 5 and Mockito test suites attaining 94% test coverage.",
                            "Java 17, Spring Boot, Spring Data JPA, Redis, MySQL, JUnit 5, GitHub Actions",
                            2
                    ),

                    new Experience(
                            "Junior Java Developer",
                            "DataWave Technologies",
                            "https://example.com",
                            "San Jose, CA",
                            "2019 - 2021",
                            "Developed web APIs, maintained client-facing dashboards using Spring Boot and Thymeleaf, and resolved critical production bug fixes. Automated internal reporting tools reducing manual processing by 15 hours per week.",
                            "Java, Spring MVC, Hibernate, JavaScript, CSS, HTML, PostgreSQL",
                            3
                    )
            ));
        }
    }

    private void initEducation() {

        if (educationRepository.count() == 0) {

            educationRepository.saveAll(Arrays.asList(

                    new Education(
                            "Bachelor of Science in Computer Science",
                            "University of California / Tech Institute",
                            "2015 - 2019",
                            "Graduated with Honors (Magna Cum Laude). Focus on Data Structures, Algorithms, Distributed Systems, and Database Management.",
                            1
                    ),

                    new Education(
                            "AWS Certified Solutions Architect – Associate",
                            "Amazon Web Services (AWS)",
                            "2023",
                            "Validation of expertise in designing highly available, cost-efficient, and secure cloud architectures on AWS.",
                            2
                    ),

                    new Education(
                            "Oracle Certified Professional: Java SE 17 Developer",
                            "Oracle Corporation",
                            "2022",
                            "Comprehensive certification covering modern Java functional programming, concurrency, streams, and modular architectures.",
                            3
                    )
            ));
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