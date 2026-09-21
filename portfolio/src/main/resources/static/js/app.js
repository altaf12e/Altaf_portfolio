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
    initTypingAnimation();
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
    const bars = document.querySelectorAll('.skill-progress-fill');
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

