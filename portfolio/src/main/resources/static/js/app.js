/**
 * Full-Stack Portfolio Application Interactivity
 * Handles Dark/Light theme toggle, live project search & filtering,
 * asynchronous REST API like counter, and smooth UI animations.
 */

document.addEventListener('DOMContentLoaded', () => {
    initThemeToggle();
    initMobileMenu();
    initProjectLikes();
    initProjectFilters();
    initSkillProgressBars();
    initStatsCounter();
    initTypingAnimation();
    initEmailActions();
});

/* ================= Theme Toggle ================= */
function initThemeToggle() {
    const themeBtn = document.getElementById('theme-toggle-btn');
    if (!themeBtn) return;

    const savedTheme = localStorage.getItem('portfolio-theme');
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    
    if (savedTheme === 'light' || (!savedTheme && !prefersDark)) {
        document.documentElement.setAttribute('data-theme', 'light');
        themeBtn.textContent = '🌙 Dark';
    } else {
        document.documentElement.removeAttribute('data-theme');
        themeBtn.textContent = '☀️ Light';
    }

    themeBtn.addEventListener('click', () => {
        const isCurrentLight = document.documentElement.getAttribute('data-theme') === 'light';
        if (isCurrentLight) {
            document.documentElement.removeAttribute('data-theme');
            localStorage.setItem('portfolio-theme', 'dark');
            themeBtn.textContent = '☀️ Light';
        } else {
            document.documentElement.setAttribute('data-theme', 'light');
            localStorage.setItem('portfolio-theme', 'light');
            themeBtn.textContent = '🌙 Dark';
        }
    });
}

/* ================= Mobile Menu Toggle ================= */
function initMobileMenu() {
    const menuToggle = document.getElementById('mobile-menu-toggle');
    const navLinks = document.getElementById('nav-links');
    if (!menuToggle || !navLinks) return;

    menuToggle.addEventListener('click', () => {
        navLinks.classList.toggle('open');
    });

    document.addEventListener('click', (e) => {
        if (!navLinks.contains(e.target) && !menuToggle.contains(e.target)) {
            navLinks.classList.remove('open');
        }
    });
}

/* ================= Project Like Button (AJAX) ================= */
function initProjectLikes() {
    const likeButtons = document.querySelectorAll('.like-btn');
    likeButtons.forEach(btn => {
        const projectId = btn.getAttribute('data-project-id');
        const storageKey = `project_liked_${projectId}`;
        
        if (localStorage.getItem(storageKey)) {
            btn.classList.add('liked');
        }

        btn.addEventListener('click', async (e) => {
            e.preventDefault();
            if (btn.classList.contains('liked')) {
                return; // Already liked in this browser session
            }

            try {
                const response = await fetch(`/api/v1/projects/${projectId}/like`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    const countSpan = btn.querySelector('.like-count');
                    if (countSpan && data.likes !== undefined) {
                        countSpan.textContent = data.likes;
                    }
                    btn.classList.add('liked');
                    localStorage.setItem(storageKey, 'true');
                    
                    // Small celebration pulse
                    btn.style.transform = 'scale(1.2)';
                    setTimeout(() => btn.style.transform = '', 200);
                }
            } catch (err) {
                console.error('Failed to register like:', err);
            }
        });
    });
}

/* ================= Project Category Filter & Live Search ================= */
function initProjectFilters() {
    const filterPills = document.querySelectorAll('.filter-pill');
    const searchInput = document.getElementById('project-search-input');
    const projectCards = document.querySelectorAll('.project-card');

    if (!projectCards.length) return;

    let activeCategory = 'All';
    let searchQuery = '';

    function applyFilters() {
        projectCards.forEach(card => {
            const cardCategory = (card.getAttribute('data-category') || '').toLowerCase();
            const cardTitle = (card.querySelector('.project-title')?.textContent || '').toLowerCase();
            const cardDesc = (card.querySelector('.project-desc')?.textContent || '').toLowerCase();
            const cardTags = (card.querySelector('.tags')?.textContent || '').toLowerCase();

            const matchesCategory = (activeCategory === 'All' || cardCategory === activeCategory.toLowerCase());
            const matchesQuery = !searchQuery || 
                cardTitle.includes(searchQuery) || 
                cardDesc.includes(searchQuery) || 
                cardTags.includes(searchQuery);

            if (matchesCategory && matchesQuery) {
                card.style.display = 'flex';
            } else {
                card.style.display = 'none';
            }
        });
    }

    filterPills.forEach(pill => {
        pill.addEventListener('click', () => {
            filterPills.forEach(p => p.classList.remove('active'));
            pill.classList.add('active');
            activeCategory = pill.getAttribute('data-filter') || 'All';
            applyFilters();
        });
    });

    if (searchInput) {
        searchInput.addEventListener('input', (e) => {
            searchQuery = e.target.value.trim().toLowerCase();
            applyFilters();
        });
    }
}

/* ================= Animated Skill Progress Bars ================= */
function initSkillProgressBars() {
    const bars = document.querySelectorAll('.skill-progress-fill, .skill-bar-fill');
    if (!bars.length) return;

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const targetWidth = entry.target.getAttribute('data-width');
                if (targetWidth) {
                    entry.target.style.width = targetWidth + '%';
                }
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.2 });

    bars.forEach(bar => {
        bar.style.width = '0%';
        observer.observe(bar);
    });
}

/* ================= Animated Stats Counter ================= */
function initStatsCounter() {
    const counts = document.querySelectorAll('.stat .count');
    if (!counts.length) return;

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const target = +entry.target.getAttribute('data-target');
                if (!isNaN(target)) {
                    let current = 0;
                    const step = Math.max(1, Math.ceil(target / 25));
                    const timer = setInterval(() => {
                        current = Math.min(current + step, target);
                        entry.target.textContent = current + '+';
                        if (current >= target) clearInterval(timer);
                    }, 40);
                }
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.2 });

    counts.forEach(el => observer.observe(el));
}

/* ================= Typing Animation for Hero Subtitle ================= */
function initTypingAnimation() {
    const typedEl = document.getElementById('typed');
    if (!typedEl) return;

    const words = [
        'CS Student',
        'Full Stack Developer',
        'Spring Boot Engineer',
        'Problem Solver'
    ];
    let wordIdx = 0;
    let charIdx = 0;
    let isDeleting = false;
    let typeSpeed = 110;

    function type() {
        const currentWord = words[wordIdx];
        if (isDeleting) {
            typedEl.textContent = currentWord.substring(0, charIdx - 1);
            charIdx--;
            typeSpeed = 45;
        } else {
            typedEl.textContent = currentWord.substring(0, charIdx + 1);
            charIdx++;
            typeSpeed = 100;
        }

        if (!isDeleting && charIdx === currentWord.length) {
            isDeleting = true;
            typeSpeed = 2200; // Pause when word is completely typed
        } else if (isDeleting && charIdx === 0) {
            isDeleting = false;
            wordIdx = (wordIdx + 1) % words.length;
            typeSpeed = 400; // Pause before starting next word
        }

        setTimeout(type, typeSpeed);
    }
    type();
}

/* ================= Email Action & Toast Handler ================= */
function initEmailActions() {
    const emailButtons = document.querySelectorAll('.email-action-btn, a[href^="mailto:"]');

    emailButtons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const href = btn.getAttribute('href');
            if (!href || !href.startsWith('mailto:')) return;

            // Extract email and subject
            const mailtoMatch = href.match(/^mailto:([^?]+)(?:\?subject=([^&]*))?/);
            const targetEmail = mailtoMatch ? decodeURIComponent(mailtoMatch[1]) : 'altafhussain078692@gmail.com';
            const subject = mailtoMatch && mailtoMatch[2] ? decodeURIComponent(mailtoMatch[2]) : 'Portfolio Inquiry - Altaf Hussain';

            // Show helpful fallback toast in case OS has no desktop mail client configured
            showEmailToast(targetEmail, subject);
        });
    });
}

function showEmailToast(email, subject) {
    const existingToast = document.getElementById('email-toast');
    if (existingToast) existingToast.remove();

    const gmailWebUrl = `https://mail.google.com/mail/?view=cm&fs=1&to=${encodeURIComponent(email)}&su=${encodeURIComponent(subject)}`;

    const toast = document.createElement('div');
    toast.id = 'email-toast';
    toast.className = 'email-toast';
    toast.innerHTML = `
        <div class="email-toast-header">
            <span>✉️ <strong>Opening Mail App...</strong></span>
            <button type="button" class="email-toast-close" title="Close" aria-label="Close">✕</button>
        </div>
        <p class="email-toast-body">Attempting to open your default mail app. If it didn't open:</p>
        <div class="email-toast-actions">
            <a href="${gmailWebUrl}" target="_blank" rel="noopener" class="email-toast-btn primary">
                <i class="fab fa-google"></i> Open Gmail Web ↗
            </a>
            <button type="button" class="email-toast-btn secondary copy-email-btn">
                📋 Copy Email
            </button>
        </div>
    `;

    document.body.appendChild(toast);

    // Close button
    toast.querySelector('.email-toast-close').addEventListener('click', () => {
        toast.remove();
    });

    // Copy email button
    const copyBtn = toast.querySelector('.copy-email-btn');
    copyBtn.addEventListener('click', () => {
        if (navigator.clipboard) {
            navigator.clipboard.writeText(email).then(() => {
                copyBtn.innerHTML = '✓ Copied!';
                setTimeout(() => { copyBtn.innerHTML = '📋 Copy Email'; }, 2500);
            });
        }
    });

    // Auto dismiss after 10s
    setTimeout(() => {
        if (document.body.contains(toast)) {
            toast.remove();
        }
    }, 10000);
}

