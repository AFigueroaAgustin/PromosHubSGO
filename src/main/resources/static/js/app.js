// ==========================================================================
// Punto Promo SGO — Lógica de Interfaz y Comportamiento (Frontend)
// ==========================================================================

// --- Subtema: Gestión de Tema (Claro / Oscuro) ---
function updateThemeUI(theme) {
    const icon = document.getElementById('themeIcon');
    const text = document.getElementById('themeText');
    if (icon && text) {
        if (theme === 'light') {
            icon.className = 'bi bi-moon-stars-fill text-primary';
            text.innerText = 'Modo Oscuro';
        } else {
            icon.className = 'bi bi-sun-fill text-warning';
            text.innerText = 'Modo Claro';
        }
    }
}

function applyTheme(theme) {
    document.documentElement.setAttribute('data-bs-theme', theme);
    localStorage.setItem('promohub_theme', theme);
    updateThemeUI(theme);
}

function toggleTheme() {
    const current = document.documentElement.getAttribute('data-bs-theme') || 'dark';
    applyTheme(current === 'dark' ? 'light' : 'dark');
}

// --- Subtema: Botón Flotante (Volver Arriba) ---
function subirArriba() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

function initScrollTop() {
    const btn = document.getElementById('btnScrollTop');
    if (btn) {
        window.addEventListener('scroll', () => {
            if (window.scrollY > 300) {
                btn.classList.add('show');
            } else {
                btn.classList.remove('show');
            }
        });
    }
}

// --- Inicialización del DOM ---
document.addEventListener('DOMContentLoaded', () => {
    const savedTheme = localStorage.getItem('promohub_theme') || 'dark';
    updateThemeUI(savedTheme);
    initScrollTop();
});
