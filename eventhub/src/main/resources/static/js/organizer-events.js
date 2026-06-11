document.addEventListener('DOMContentLoaded', () => {

    const role = sessionStorage.getItem('userRole');
    if (role !== 'ROLE_ORGANIZER' && role !== 'ROLE_ADMIN') {
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

    document.getElementById('btn-salva').addEventListener('click', salvaEvento);
    document.getElementById('btn-annulla').addEventListener('click', resetForm);

    loadMieiEventi();
});

async function loadMieiEventi() {
    const authHeader = sessionStorage.getItem('authHeader');
    const username = sessionStorage.getItem('username');
    const oggi = new Date().toISOString().split('T')[0];

    try {
        const response = await fetch(`/api/events/username?username=${username}`, {
            method: 'GET',
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        if (response.ok) {
            const events = await response.json();
            const lista = document.getElementById('lista-eventi-organizer');
            lista.innerHTML = '';

            if (events.length === 0) {
                lista.innerHTML = '<p>Nessun evento creato.</p>';
                return;
            }

            for (const event of events) {
                const div = document.createElement('div');
                div.className = 'event-card';

                const speakerNomi = event.speakerNames?.join(', ') || 'N/D';
                const tagNomi = event.tagNames?.join(', ') || 'N/D';
                const eventoPassato = event.date < oggi;

                const ratingResp = await fetch(`/api/events/${event.id}/rating`, {
                    headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
                });
                const rating = ratingResp.ok ? await ratingResp.json() : 0;

                const feedbackResp = await fetch(`/api/feedBacks/event/${event.id}`, {
                    headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
                });
                const feedbacks = feedbackResp.ok ? await feedbackResp.json() : [];

                const ticketsResp = await fetch(`/api/tickets/event/${event.id}`, {
                    headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
                });
                const tickets = ticketsResp.ok ? await ticketsResp.json() : [];

                let feedbackHtml = '';
                if (feedbacks.length > 0) {
                    feedbackHtml = '<h4>Feedback ricevuti:</h4>';
                    feedbacks.forEach(f => {
                        feedbackHtml += `<p>${'⭐'.repeat(f.rating)} — ${f.comment || ''}</p>`;
                    });
                }

                let partecipantiHtml = '';
                if (tickets.length > 0) {
                    partecipantiHtml = '<h4>Partecipanti:</h4>';
                    tickets.forEach(t => {
                        partecipantiHtml += `<p>👤 ${t.username || 'Utente'} — ${t.type}</p>`;
                    });
                }

                div.innerHTML = `
                    <h3>${event.title}</h3>
                    <p><strong>Data:</strong> ${event.date}</p>
                    <p><strong>Sede:</strong> ${event.venueName || 'N/D'}</p>
                    <p><strong>Relatori:</strong> ${speakerNomi}</p>
                    <p><strong>Argomenti:</strong> ${tagNomi}</p>
                    ${eventoPassato
                        ? '<p><strong>🔴 Evento concluso</strong></p>'
                        : `<p><strong>Posti totali:</strong> ${event.maxSeats}</p>
                           <p><strong>Posti disponibili:</strong> ${event.availableSeats}</p>`
                    }
                    <p><strong>Prezzo Standard:</strong> €${event.standardPrice ?? 'N/D'}</p>
                    <p><strong>Prezzo VIP:</strong> €${event.vipPrice ?? 'N/D'}</p>
                    <p><strong>Valutazione media:</strong> ${rating > 0 ? '⭐'.repeat(rating) : 'Nessuna recensione'}</p>
                    ${feedbackHtml}
                    ${partecipantiHtml}
                    ${eventoPassato
                        ? ''
                        : `<button onclick="modificaEvento(${event.id})">Modifica</button>
                           <button onclick="eliminaEvento(${event.id})" style="background-color:#e74c3c;">Elimina</button>`
                    }
                `;
                lista.appendChild(div);
            }
        }
    } catch (error) {
        console.error('Errore di connessione:', error);
    }
}

async function salvaEvento() {
    const authHeader = sessionStorage.getItem('authHeader');
    const msg = document.getElementById('msg-form');
    const eventoId = document.getElementById('evento-id').value;

    const tagIdsRaw = document.getElementById('input-tagIds').value.trim();
    const speakerIdsRaw = document.getElementById('input-speakerIds').value.trim();

    const dto = {
        title: document.getElementById('input-title').value.trim(),
        description: document.getElementById('input-description').value.trim(),
        date: document.getElementById('input-date').value,
        maxSeats: parseInt(document.getElementById('input-maxSeats').value) || null,
        venueId: parseInt(document.getElementById('input-venueId').value) || null,
        standardPrice: parseInt(document.getElementById('input-standardPrice').value) || 0,
        vipPrice: parseInt(document.getElementById('input-vipPrice').value) || 0,
        tagIds: tagIdsRaw ? tagIdsRaw.split(',').map(id => parseInt(id.trim())) : [],
        speakerIds: speakerIdsRaw ? speakerIdsRaw.split(',').map(id => parseInt(id.trim())) : []
    };

    if (!dto.title || !dto.description || !dto.date || !dto.maxSeats) {
        msg.style.color = 'red';
        msg.textContent = 'Compila tutti i campi obbligatori.';
        return;
    }

    const isModifica = eventoId !== '';
    const url = isModifica ? `/api/events/${eventoId}` : '/api/events';
    const method = isModifica ? 'PUT' : 'POST';

    try {
        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = isModifica ? 'Evento aggiornato!' : 'Evento creato!';
            resetForm();
            loadMieiEventi();
        } else {
            const err = await response.json().catch(() => null);
            msg.style.color = 'red';
            msg.textContent = err?.message || 'Errore durante il salvataggio.';
        }
    } catch (error) {
        console.error('Errore di connessione:', error);
    }
}

function modificaEvento(id) {
    const authHeader = sessionStorage.getItem('authHeader');
    fetch(`/api/events/${id}`, {
        headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
    })
    .then(r => r.json())
    .then(event => {
        document.getElementById('form-titolo').textContent = 'Modifica evento';
        document.getElementById('evento-id').value = event.id;
        document.getElementById('input-title').value = event.title;
        document.getElementById('input-description').value = event.description;
        document.getElementById('input-date').value = event.date;
        document.getElementById('input-maxSeats').value = event.maxSeats;
        document.getElementById('input-venueId').value = event.venueId || '';
        document.getElementById('input-standardPrice').value = event.standardPrice || '';
        document.getElementById('input-vipPrice').value = event.vipPrice || '';
        document.getElementById('input-tagIds').value = event.tagIds?.join(',') || '';
        document.getElementById('input-speakerIds').value = event.speakerIds?.join(',') || '';
        document.getElementById('btn-annulla').style.display = 'inline';
        document.getElementById('msg-form').textContent = '';
        window.scrollTo(0, 0);
    });
}

function resetForm() {
    document.getElementById('form-titolo').textContent = 'Crea nuovo evento';
    document.getElementById('evento-id').value = '';
    document.getElementById('input-title').value = '';
    document.getElementById('input-description').value = '';
    document.getElementById('input-date').value = '';
    document.getElementById('input-maxSeats').value = '';
    document.getElementById('input-venueId').value = '';
    document.getElementById('input-standardPrice').value = '';
    document.getElementById('input-vipPrice').value = '';
    document.getElementById('input-tagIds').value = '';
    document.getElementById('input-speakerIds').value = '';
    document.getElementById('btn-annulla').style.display = 'none';
    document.getElementById('msg-form').textContent = '';
}

async function eliminaEvento(id) {
    if (!confirm('Sei sicuro di voler eliminare questo evento?')) return;
    const authHeader = sessionStorage.getItem('authHeader');
    try {
        const response = await fetch(`/api/events/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': authHeader, 'X-Requested-With': 'XMLHttpRequest' }
        });
        if (response.ok || response.status === 204) loadMieiEventi();
        else alert('Errore durante l\'eliminazione.');
    } catch (error) {
        console.error('Errore di connessione:', error);
    }
}