# 🚀 Full-Stack Spring Boot Portfolio & Management CMS

A complete, production-ready portfolio and project showcase application built with **Spring Boot 3**, **Spring Data JPA**, **Spring Security**, and **Thymeleaf**. It features an out-of-the-box embedded/file-persisted database (H2) with immediate MySQL compatibility, a secured Admin CMS portal, interactive REST APIs, and a modern responsive dark/light frontend.

---

## 🌟 Key Features

### 🖥️ Public Showcase
- **Glassmorphism & Theme Engine**: Instant Dark / Light mode toggle with `localStorage` and system preference detection.
- **Hero & Identity**: Dynamic status indicator (*"Available for new projects"*), headline, tagline, and call-to-action buttons.
- **Interactive Project Showcase**:
  - Filter by category (*All*, *Backend*, *Fullstack*, *Cloud / DevOps*, *AI / Tools*).
  - Real-time instant search across titles, descriptions, and tech stacks.
  - Interactive **Like Heart Counter** with asynchronous REST API persistence.
  - Dedicated project case-study detail pages (`/projects/{id}`).
- **Interactive Technical Skills Matrix**: Grouped by category with animated progress bars on scroll.
- **Career & Education Timeline**: Visual milestone cards detailing responsibilities, achievements, and tech stack tags.
- **Interactive & Printable Resume (`/resume`)**: Formatted CV layout with instant *"Print / Save as PDF"* support (`@media print`).
- **Developer Tech Blog (`/blog`)**: Technical articles showcase with read time estimates, tags, and reading view.
- **Client Testimonials & Endorsements**: Star-rated reviews.
- **Contact Form**: Validated contact form with database persistence and success feedback alerts.

### ⚙️ Secured Admin CMS Portal (`/admin`)
- Protected by **Spring Security** (Role: `ROLE_ADMIN`).
- **Analytics Overview (`/admin/dashboard`)**: Total site visits, unread inquiries counter, community likes, and total projects.
- **Projects Management (`/admin/projects`)**: Full CRUD (Create, Read, Update, Delete) with category assignment and homepage featured toggle.
- **Skills Management (`/admin/skills`)**: Interactive proficiency slider and category manager.
- **Inquiries Inbox (`/admin/messages`)**: Read contact submissions, mark as read/unread, delete, or quick-reply via direct email links.
- **Profile & Site Settings (`/admin/settings`)**: Edit public name, bio, social profiles, email, phone, and availability status.

### 🔌 RESTful API Endpoints (`/api/v1/...`)
- `POST /api/v1/projects/{id}/like` — Increment project likes asynchronously.
- `GET /api/v1/projects` — JSON array of projects (supports `?category=` and `?q=`).
- `GET /api/v1/projects/{id}` — Single project details.
- `GET /api/v1/skills` — Skills grouped by category.
- `POST /api/v1/contact` — Submit contact inquiry via JSON.
- `GET /api/v1/stats` — Platform live metrics (visits, likes, project counts).

---

## 🔐 Default Admin Credentials

- **URL**: [http://localhost:8080/admin/login](http://localhost:8080/admin/login)
- **Username**: `admin`
- **Password**: `admin123`
*(Configurable anytime in `src/main/resources/application.properties`)*

---

## 🗄️ Database Setup & H2 Console

### Default (Embedded Persistent H2 Database)
The project is configured to run **instantly** without needing MySQL or any database installed on your machine!
- Database file is auto-stored in `./data/portfoliodb`.
- Automatic realistic seed data is populated on the first run via `DataInitializer`.
- **H2 Web Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:file:./data/portfoliodb`
  - User: `sa`
  - Password: *(leave blank)*

### Optional: Switching to MySQL
To use MySQL instead:
1. Open `src/main/resources/application.properties`.
2. Uncomment the MySQL configuration section:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/portfolio?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=your_password
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
   ```

---

## 🚀 How to Run the Application

### Option 1: Using Eclipse / STS / IntelliJ IDEA / VS Code
1. Open the project folder `portfolio` in your IDE.
2. Allow Maven to update project dependencies.
3. Locate `PortfolioApplication.java` inside `src/main/java/com/portfolio/app/`.
4. Right-click &rarr; **Run As &rarr; Spring Boot App** (or **Run 'PortfolioApplication.main()'**).
5. Open your browser and navigate to:
   - Public Website: [http://localhost:8080](http://localhost:8080)
   - Admin CMS: [http://localhost:8080/admin](http://localhost:8080/admin)

### Option 2: Using Maven Command Line
In the `portfolio` folder:
```bash
mvn clean spring-boot:run
```
Or package a standalone executable JAR:
```bash
mvn clean package
java -jar target/portfolio-app-1.0.0.jar
```

---

## 📁 Project Structure

```
portfolio/
├── pom.xml                                    # Spring Boot 3 dependencies (JPA, Security, H2, MySQL, Thymeleaf)
└── src/main/
    ├── java/com/portfolio/app/
    │   ├── PortfolioApplication.java          # Spring Boot main entrypoint
    │   ├── config/
    │   │   ├── SecurityConfig.java            # Spring Security authorization rules
    │   │   └── DataInitializer.java           # Realistic sample seed data runner
    │   ├── controller/
    │   │   ├── PortfolioController.java       # Public MVC routes (/about, /projects, /resume, /blog, /contact)
    │   │   ├── PortfolioApiController.java    # REST API endpoints (/api/v1/...)
    │   │   └── AdminController.java           # Secured Admin CMS (/admin/dashboard, /projects, /messages)
    │   ├── model/
    │   │   ├── Project.java                   # JPA entity with likes, category, tags
    │   │   ├── Skill.java                     # JPA entity with proficiency, category
    │   │   ├── Experience.java                # Career timeline entity
    │   │   ├── Education.java                 # Degree & certification entity
    │   │   ├── Testimonial.java               # Client review entity
    │   │   ├── BlogPost.java                  # Dev blog article entity
    │   │   ├── ContactMessage.java            # Form inquiry entity
    │   │   └── ProfileSettings.java           # Site metadata & visitor counter entity
    │   ├── repository/                        # Spring Data JPA repositories
    │   └── service/                           # Business logic service layer
    └── resources/
        ├── application.properties             # Database, H2 console, and admin config
        ├── static/
        │   ├── css/style.css                  # Modern design system (Dark/Light mode, Glassmorphism, Print)
        │   └── js/app.js                      # Theme switcher, live search, AJAX like button
        └── templates/
            ├── fragments/layout.html          # Dynamic navbar & footer
            ├── index.html                     # Hero, stats banner, featured projects, reviews
            ├── projects.html                  # Filterable project catalog with live search
            ├── project-detail.html            # Case study viewer
            ├── about.html                     # Skills matrix & career timeline
            ├── resume.html                    # Printable CV viewer
            ├── blog.html                      # Technical blog listing
            ├── blog-detail.html               # Article reading page
            ├── contact.html                   # Contact form & info cards
            └── admin/
                ├── layout.html                # CMS navigation & sidebar
                ├── login.html                 # Admin login screen
                ├── dashboard.html             # Metric KPI cards & recent inquiries
                ├── projects.html              # Project CRUD table
                ├── project-form.html          # Add/edit project form
                ├── skills.html                # Skills manager & slider
                ├── messages.html              # Contact inquiries inbox
                └── settings.html              # Profile & site configuration
```

