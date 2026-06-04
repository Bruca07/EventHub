function getAuthHeader() {
    return sessionStorage.getItem('authHeader');
}

async function apiFetch(url, options = {}) {
    const authHeader = getAuthHeader();
    const headers = {
        'Content-Type': 'application/json',
        'X-Requested-With': 'XMLHttpRequest',
        ...(authHeader ? { 'Authorization': authHeader } : {}),
        ...(options.headers || {})
    };
    return fetch(url, { ...options, headers });
}

document.addEventListener('DOMContentLoaded', () => {

    // LOGIN
    const loginForm = document.getElementById('loginForm');
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const username = loginForm.username.value.trim();
            const password = loginForm.password.value;
            const authHeader = 'Basic ' + btoa(`${username}:${password}`);
            sessionStorage.setItem('authHeader', authHeader);

            try {
                const response = await apiFetch('/api/users');

                if (response.status === 401) {
                    sessionStorage.clear();
                    alert('Credenziali errate');
                    return;
                }

                const users = await response.json();
                const currentUser = users.find(u => u.username === username);

                if (!currentUser) {
                    sessionStorage.clear();
                    alert('Credenziali errate');
                    return;
                }

                sessionStorage.setItem('userRole', currentUser.role.name);
                sessionStorage.setItem('username', currentUser.username);

                if (currentUser.role.name === 'ROLE_ADMIN') {
                    window.location.href = '/admin.html';
                } else if (currentUser.role.name === 'ROLE_ORGANIZER') {
                    window.location.href = '/organizer-events.html';
                } else {
                    window.location.href = '/index.html';
                }
            } catch (err) {
                console.error('Errore login:', err);
                alert('Errore di connessione al server');
            }
        });
    }

    // SIGNUP
    const signupForm = document.getElementById('signupForm');
    if (signupForm) {
        signupForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const data = {
                username: signupForm.username.value.trim(),
                password: signupForm.password.value
            };

            try {
                const response = await fetch('/api/auth/signup', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-Requested-With': 'XMLHttpRequest'
                    },
                    body: JSON.stringify(data)
                });

                if (response.ok) {
                    const msg = document.getElementById('msg-signup');
                    if (msg) {
                        msg.style.color = 'green';
                        msg.textContent = 'Registrazione completata! Reindirizzamento al login...';
                    }
                    setTimeout(() => {
                        window.location.href = '/login.html';
                    }, 2000);
                } else {
                    const msg = document.getElementById('msg-signup');
                    if (msg) {
                        msg.style.color = 'red';
                        msg.textContent = 'Errore durante la registrazione.';
                    }
                }
            } catch (err) {
                console.error(err);
                alert('Errore di connessione.');
            }
        });
    }

    // LOGOUT
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            sessionStorage.clear();
            window.location.href = '/login.html';
        });
    }
});