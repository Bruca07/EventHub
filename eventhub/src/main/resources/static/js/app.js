document.addEventListener('DOMContentLoaded', () => {
    const authHeader = sessionStorage.getItem('authHeader');
    const role = sessionStorage.getItem('userRole');

    const loginLink = document.getElementById('login-link');
    const signupLink = document.getElementById('signup-link');
    const logoutLink = document.getElementById('logout-link');
    const bookingsLink = document.getElementById('bookings-link');
    const adminLink = document.getElementById('admin-link');
    const profileLink = document.getElementById('profile-link');

    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            sessionStorage.clear();
            window.location.href = 'index.html';
        });
    }

    if (!authHeader) {
        if (loginLink) loginLink.style.display = 'inline';
        if (signupLink) signupLink.style.display = 'inline';
        if (logoutLink) logoutLink.style.display = 'none';
        if (bookingsLink) bookingsLink.style.display = 'none';
        if (adminLink) adminLink.style.display = 'none';
        if (profileLink) profileLink.style.display = 'none';
    } else {
        if (loginLink) loginLink.style.display = 'none';
        if (signupLink) signupLink.style.display = 'none';
        if (logoutLink) logoutLink.style.display = 'inline';
        if (bookingsLink) bookingsLink.style.display = 'inline';
        if (profileLink) profileLink.style.display = 'inline';
        if (adminLink) adminLink.style.display = (role === 'ROLE_ADMIN') ? 'inline' : 'none';
    }

    document.getElementById('btn-filtra').addEventListener('click', () => {
        const date = document.getElementById('filtro-data-inizio').value;
        const tag = document.getElementById('filtro-tag').value;
        const city = document.getElementById('filtro-city').value;
        loadEvents(date, tag, city);
    });

    loadEvents();
});

async function loadEvents(date = '', tag = '', city = '') {
    let url = '/api/events';
    if (tag) url = `/api/events/TagsName?name=${encodeURIComponent(tag)}`;
    else if (city) url = `/api/events/city?city=${encodeURIComponent(city)}`;
    else if (date) url = `/api/events/date?date=${date}`;

    const headers = { 'Content-Type': 'application/json' };
    const authHeader = sessionStorage.getItem('authHeader');
    if (authHeader) headers['Authorization'] = authHeader;

    try {
        const response = await fetch(url, { method: 'GET', headers });

        if (response.status === 401) {
            sessionStorage.clear();
            window.location.href = 'login.html';
            return;
        }

        const data = await response.json();
        const events = data.content ?? data;
        const eventList = document.getElementById('event-list');

        eventList.innerHTML = (!events || events.length === 0)
            ? '<p>Nessun evento trovato.</p>'
            : '';

        const oggi = new Date().toISOString().split('T')[0];

        events.forEach(event => {
            const div = document.createElement('div');
            div.className = 'event-card';

            const speakerNomi = event.speakerNames && event.speakerNames.length > 0
                ? event.speakerNames.join(', ')
                : 'N/D';

            const tagNomi = event.tagNames && event.tagNames.length > 0
                ? event.tagNames.join(', ')
                : 'N/D';

            const eventoPassato = event.date < oggi;

            div.innerHTML = `
                <h3>${event.title ?? 'Evento'}</h3>
                <p><strong>Data:</strong> ${event.date ?? ''}</p>
                <p><strong>Sede:</strong> ${event.venueName ?? 'N/D'} ${event.venueCity ? '— ' + event.venueCity : ''}</p>
                <p><strong>Relatori:</strong> ${speakerNomi}</p>
                <p><strong>Argomenti:</strong> ${tagNomi}</p>
                ${eventoPassato
                    ? '<p><strong>Evento concluso</strong></p>'
                    : `<p><strong>Posti disponibili:</strong> ${event.availableSeats ?? ''}</p>`
                }
                <p>${event.description ?? ''}</p>
                <a href="event-detail.html?id=${event.id}">Dettagli</a>
            `;
            eventList.appendChild(div);
        });
    } catch (error) {
        console.error("Errore di connessione:", error);
    }
}