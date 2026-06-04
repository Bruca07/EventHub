document.addEventListener('DOMContentLoaded', () => {

    const role = sessionStorage.getItem('userRole');
    if (role !== 'ROLE_ADMIN') {
        alert('Accesso non autorizzato.');
        window.location.href = 'index.html';
        return;
    }

    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            sessionStorage.clear();
            window.location.href = 'login.html';
        });
    }

    loadUtenti();
    loadVenue();
    loadSpeaker();
    loadTag();
    loadFeedbacks();
});

function showTab(tab) {
    document.querySelectorAll('.tab-content').forEach(el => el.style.display = 'none');
    document.getElementById('tab-' + tab).style.display = 'block';
}

// ==================== UTENTI ====================

async function loadUtenti() {
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch('/api/users', {
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) {
            const utenti = await response.json();
            const lista = document.getElementById('lista-utenti');
            lista.innerHTML = '';
            utenti.forEach(u => {
                const div = document.createElement('div');
                div.className = 'event-card';
                div.innerHTML = `
                    <p>
                        <strong>${u.username}</strong> — ${u.role.name}
                        <br>Stato: ${u.enabled ? "🟢 ATTIVO" : "🔴 BANNATO"}
                    </p>
                    <select id="role-select-${u.id}">
                        <option value="ROLE_USER" ${u.role.name === 'ROLE_USER' ? 'selected' : ''}>USER</option>
                        <option value="ROLE_ORGANIZER" ${u.role.name === 'ROLE_ORGANIZER' ? 'selected' : ''}>ORGANIZER</option>
                        <option value="ROLE_ADMIN" ${u.role.name === 'ROLE_ADMIN' ? 'selected' : ''}>ADMIN</option>
                    </select>
                    <button onclick="cambiaRuolo(${u.id})">Cambia ruolo</button>
                    <button onclick="toggleBan(${u.id})" style="background-color:${u.enabled ? '#e74c3c' : '#2ecc71'};">
                        ${u.enabled ? 'Banna' : 'Riattiva'}
                    </button>
                    <button onclick="eliminaUtente(${u.id})" style="background-color:#e74c3c;">Elimina</button>
                `;
                lista.appendChild(div);
            });
        }
    } catch (error) { console.error('Errore caricamento utenti:', error); }
}

async function cambiaRuolo(userId) {
    const authHeader = sessionStorage.getItem('authHeader');
    const nuovoRuolo = document.getElementById(`role-select-${userId}`).value;
    try {
        const response = await fetch(`/api/users/${userId}/role?name=${nuovoRuolo}`, {
            method: 'PUT',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) loadUtenti();
        else alert('Errore cambio ruolo');
    } catch (error) { console.error(error); }
}

async function toggleBan(userId) {
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/users/${userId}/ban`, {
            method: 'PUT',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) loadUtenti();
        else alert('Errore ban/unban');
    } catch (error) { console.error(error); }
}

async function eliminaUtente(userId) {
    if (!confirm('Sei sicuro?')) return;
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok || response.status === 204) loadUtenti();
        else alert('Errore eliminazione');
    } catch (error) { console.error(error); }
}

// ==================== VENUE ====================

async function loadVenue() {
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch('/api/venues', {
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) {
            const venues = await response.json();
            const lista = document.getElementById('lista-venue');
            lista.innerHTML = '';
            venues.forEach(v => {
                const div = document.createElement('div');
                div.className = 'event-card';
                div.innerHTML = `
                    <p><strong>${v.name}</strong> — ${v.city} — ${v.address} — Capienza: ${v.capacity}</p>
                    <button onclick="modificaVenue(${v.id}, '${v.name}', '${v.address}', '${v.city}', ${v.capacity})">Modifica</button>
                    <button onclick="eliminaVenue(${v.id})" style="background-color:#e74c3c;">Elimina</button>
                `;
                lista.appendChild(div);
            });
        }
    } catch (error) { console.error(error); }
}

async function salvaVenue() {
    const authHeader = sessionStorage.getItem('authHeader');
    const msg = document.getElementById('msg-venue');
    const venueId = document.getElementById('venue-id').value;
    const dto = {
        name: document.getElementById('venue-name').value.trim(),
        address: document.getElementById('venue-address').value.trim(),
        city: document.getElementById('venue-city').value.trim(),
        capacity: parseInt(document.getElementById('venue-capacity').value)
    };
    const isModifica = venueId !== '';
    const url = isModifica ? `/api/venues/${venueId}` : '/api/venues';
    const method = isModifica ? 'PUT' : 'POST';
    try {
        const response = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json', 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' },
            body: JSON.stringify(dto)
        });
        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = isModifica ? 'Sede aggiornata!' : 'Sede creata!';
            resetFormVenue();
            loadVenue();
        } else {
            msg.style.color = 'red';
            msg.textContent = 'Errore durante il salvataggio.';
        }
    } catch (error) { console.error(error); }
}

function modificaVenue(id, name, address, city, capacity) {
    document.getElementById('form-venue-titolo').textContent = 'Modifica sede';
    document.getElementById('venue-id').value = id;
    document.getElementById('venue-name').value = name;
    document.getElementById('venue-address').value = address;
    document.getElementById('venue-city').value = city;
    document.getElementById('venue-capacity').value = capacity;
    document.getElementById('btn-annulla-venue').style.display = 'inline';
}

function resetFormVenue() {
    document.getElementById('form-venue-titolo').textContent = 'Aggiungi sede';
    document.getElementById('venue-id').value = '';
    document.getElementById('venue-name').value = '';
    document.getElementById('venue-address').value = '';
    document.getElementById('venue-city').value = '';
    document.getElementById('venue-capacity').value = '';
    document.getElementById('btn-annulla-venue').style.display = 'none';
    document.getElementById('msg-venue').textContent = '';
}

async function eliminaVenue(id) {
    if (!confirm('Eliminare questa sede?')) return;
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/venues/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok || response.status === 204) loadVenue();
        else alert('Errore eliminazione sede.');
    } catch (error) { console.error(error); }
}

// ==================== SPEAKER ====================

async function loadSpeaker() {
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch('/api/speakers', {
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) {
            const speakers = await response.json();
            const lista = document.getElementById('lista-speaker');
            lista.innerHTML = '';
            speakers.forEach(s => {
                const div = document.createElement('div');
                div.className = 'event-card';
                div.innerHTML = `
                    <p><strong>${s.firstName} ${s.lastName}</strong> — ${s.company}</p>
                    <button onclick="eliminaSpeaker(${s.id})" style="background-color:#e74c3c;">Elimina</button>
                `;
                lista.appendChild(div);
            });
        }
    } catch (error) { console.error(error); }
}

async function salvaSpeaker() {
    const authHeader = sessionStorage.getItem('authHeader');
    const msg = document.getElementById('msg-speaker');
    const dto = {
        firstName: document.getElementById('speaker-firstname').value.trim(),
        lastName: document.getElementById('speaker-lastname').value.trim(),
        company: document.getElementById('speaker-company').value.trim()
    };
    try {
        const response = await fetch('/api/speakers', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' },
            body: JSON.stringify(dto)
        });
        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Speaker aggiunto!';
            document.getElementById('speaker-firstname').value = '';
            document.getElementById('speaker-lastname').value = '';
            document.getElementById('speaker-company').value = '';
            loadSpeaker();
        } else {
            msg.style.color = 'red';
            msg.textContent = 'Errore durante il salvataggio.';
        }
    } catch (error) { console.error(error); }
}

async function eliminaSpeaker(id) {
    if (!confirm('Eliminare questo speaker?')) return;
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/speakers/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok || response.status === 204) loadSpeaker();
        else alert('Errore eliminazione speaker.');
    } catch (error) { console.error(error); }
}

// ==================== TAG ====================

async function loadTag() {
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch('/api/tags', {
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) {
            const tags = await response.json();
            const lista = document.getElementById('lista-tag');
            lista.innerHTML = '';
            tags.forEach(t => {
                const div = document.createElement('div');
                div.className = 'event-card';
                div.innerHTML = `
                    <p><strong>${t.name}</strong></p>
                    <button onclick="eliminaTag(${t.id})" style="background-color:#e74c3c;">Elimina</button>
                `;
                lista.appendChild(div);
            });
        }
    } catch (error) { console.error(error); }
}

async function salvaTag() {
    const authHeader = sessionStorage.getItem('authHeader');
    const msg = document.getElementById('msg-tag');
    const dto = { name: document.getElementById('tag-name').value.trim() };
    try {
        const response = await fetch('/api/tags', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' },
            body: JSON.stringify(dto)
        });
        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Tag aggiunto!';
            document.getElementById('tag-name').value = '';
            loadTag();
        } else {
            msg.style.color = 'red';
            msg.textContent = 'Errore salvataggio tag.';
        }
    } catch (error) { console.error(error); }
}

async function eliminaTag(id) {
    if (!confirm('Eliminare questo tag?')) return;
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/tags/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok || response.status === 204) loadTag();
        else alert('Errore eliminazione tag.');
    } catch (error) { console.error(error); }
}

// ==================== FEEDBACK ====================

async function loadFeedbacks() {
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch('/api/feedBacks', {
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok) {
            const feedbacks = await response.json();
            const lista = document.getElementById('lista-feedbacks');
            lista.innerHTML = '';
            if (feedbacks.length === 0) {
                lista.innerHTML = '<p>Nessun feedback presente.</p>';
                return;
            }
            feedbacks.forEach(f => {
                const div = document.createElement('div');
                div.className = 'event-card';
                div.innerHTML = `
                    <p><strong>Evento ID:</strong> ${f.eventId} — <strong>Utente ID:</strong> ${f.userId}</p>
                    <p><strong>Voto:</strong> ${'⭐'.repeat(f.rating)}</p>
                    <p>${f.comment || ''}</p>
                    <button onclick="eliminaFeedback(${f.id})" style="background-color:#e74c3c;">Elimina</button>
                `;
                lista.appendChild(div);
            });
        }
    } catch (error) { console.error(error); }
}

async function eliminaFeedback(id) {
    if (!confirm('Eliminare questo feedback?')) return;
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/feedBacks/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok || response.status === 204) loadFeedbacks();
        else alert('Errore eliminazione feedback.');
    } catch (error) { console.error(error); }
}