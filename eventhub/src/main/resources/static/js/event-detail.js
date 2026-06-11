document.addEventListener('DOMContentLoaded', () => {

    const authHeader = sessionStorage.getItem('authHeader');
    const role = sessionStorage.getItem('userRole');

    const bookingsLink = document.getElementById('bookings-link');
    const adminLink = document.getElementById('admin-link');
    const organizerLink = document.getElementById('organizer-link');
    const logoutLink = document.getElementById('logout-link');

    // Gestione navbar
    if (authHeader) {
        if (bookingsLink && role === 'ROLE_USER') bookingsLink.style.display = 'inline';
        if (adminLink && role === 'ROLE_ADMIN') adminLink.style.display = 'inline';
        if (organizerLink && role === 'ROLE_ORGANIZER') organizerLink.style.display = 'inline';
        if (logoutLink) logoutLink.style.display = 'inline';
    } else {
        if (logoutLink) logoutLink.style.display = 'none';
    }

    // Logout
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            sessionStorage.clear();
            window.location.href = 'login.html';
        });
    }

    const params = new URLSearchParams(window.location.search);
    const eventId = params.get('id');

    if (!eventId) {
        document.getElementById('evento-dettaglio').innerHTML = '<p>Evento non trovato.</p>';
        return;
    }

    loadEventDetail(eventId);

    const btnPrenota = document.getElementById('btn-prenota');
    if (btnPrenota) {
        btnPrenota.addEventListener('click', () => bookEvent(eventId));
    }
});

async function loadEventDetail(eventId) {
    const authHeader = sessionStorage.getItem('authHeader');

    try {
        const response = await fetch(`/api/events/${eventId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest',
                ...(authHeader ? { 'Authorization': authHeader } : {})
            }
        });

        if (response.ok) {
            const event = await response.json();
            const dettaglio = document.getElementById('evento-dettaglio');
            const oggi = new Date().toISOString().split('T')[0];
            const eventoPassato = event.date < oggi;

            const speakerNomi = event.speakerNames && event.speakerNames.length > 0
                ? event.speakerNames.join(', ')
                : 'N/D';

            const tagNomi = event.tagNames && event.tagNames.length > 0
                ? event.tagNames.join(', ')
                : 'N/D';

            if (eventoPassato) {
                dettaglio.innerHTML = `
                    <h2>${event.title || 'Evento'}</h2>
                    <p><strong>Data:</strong> ${event.date || 'N/D'}</p>
                    <p><strong>Sede:</strong> ${event.venueName || 'N/D'} ${event.venueCity ? '— ' + event.venueCity : ''}</p>
                    <p><strong>Relatori:</strong> ${speakerNomi}</p>
                    <p><strong>Argomenti:</strong> ${tagNomi}</p>
                    <p><strong>Descrizione:</strong> ${event.description || 'N/D'}</p>
                    <p><strong>🔴 Evento concluso</strong></p>
                `;
            } else {
                dettaglio.innerHTML = `
                    <h2>${event.title || 'Evento'}</h2>
                    <p><strong>Data:</strong> ${event.date || 'N/D'}</p>
                    <p><strong>Sede:</strong> ${event.venueName || 'N/D'} ${event.venueCity ? '— ' + event.venueCity : ''}</p>
                    <p><strong>Relatori:</strong> ${speakerNomi}</p>
                    <p><strong>Argomenti:</strong> ${tagNomi}</p>
                    <p><strong>Descrizione:</strong> ${event.description || 'N/D'}</p>
                    <p><strong>Posti totali:</strong> ${event.maxSeats ?? 'N/D'}</p>
                    <p><strong>Posti disponibili:</strong> ${event.availableSeats ?? 'N/D'}</p>
                    <p><strong>Prezzo Standard:</strong> €${event.standardPrice ?? 'N/D'}</p>
                    <p><strong>Prezzo VIP:</strong> €${event.vipPrice ?? 'N/D'}</p>
                `;

                if (authHeader && event.availableSeats > 0) {
                    document.getElementById('form-prenotazione').style.display = 'block';
                } else if (event.availableSeats === 0) {
                    dettaglio.innerHTML += '<p><strong>Posti esauriti.</strong></p>';
                }
            }

            loadFeedbacks(eventId);

        } else if (response.status === 404) {
            document.getElementById('evento-dettaglio').innerHTML = '<p>Evento non trovato.</p>';
        } else {
            document.getElementById('evento-dettaglio').innerHTML = '<p>Errore nel caricamento.</p>';
        }
    } catch (error) {
        console.error('Errore di connessione:', error);
    }
}

async function loadFeedbacks(eventId) {
    const authHeader = sessionStorage.getItem('authHeader');

    try {
        const response = await fetch(`/api/feedBacks/event/${eventId}`, {
            headers: {
                'X-Requested-With': 'XMLHttpRequest',
                ...(authHeader ? { 'Authorization': authHeader } : {})
            }
        });

        if (response.ok) {
            const feedbacks = await response.json();
            const dettaglio = document.getElementById('evento-dettaglio');

            if (feedbacks.length === 0) {
                dettaglio.innerHTML += '<h3>Recensioni</h3><p>Nessuna recensione ancora.</p>';
                return;
            }

            let feedbackHtml = '<h3>Recensioni</h3>';
            feedbacks.forEach(f => {
                feedbackHtml += `
                    <div class="feedback-card">
                        <p><strong>Voto:</strong> ${'⭐'.repeat(f.rating)}</p>
                        <p>${f.comment || ''}</p>
                    </div>
                `;
            });
            dettaglio.innerHTML += feedbackHtml;
        }
    } catch (error) {
        console.error('Errore caricamento feedback:', error);
    }
}

async function bookEvent(eventId) {
    const authHeader = sessionStorage.getItem('authHeader');

    if (!authHeader) {
        alert('Devi essere loggato per prenotare.');
        window.location.href = 'login.html';
        return;
    }

    const ticketType = document.getElementById('ticket-type').value;
    const msg = document.getElementById('msg-prenotazione');

    try {
        const response = await fetch(`/api/events/${eventId}/book`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify({
                type: ticketType,
                status: 'ACTIVE',
                eventId: parseInt(eventId),
                username: sessionStorage.getItem('username')
            })
        });

        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Prenotazione effettuata con successo!';
            loadEventDetail(eventId);
        } else if (response.status === 400) {
            const err = await response.json().catch(() => null);
            const errMsg = err?.message || '';
            msg.style.color = 'red';
            if (errMsg.includes('passato')) {
                msg.textContent = 'Prenotazione non disponibile: evento passato.';
            } else if (errMsg.includes('già prenotato')) {
                msg.textContent = 'Hai già prenotato questo evento.';
            } else {
                msg.textContent = errMsg || 'Posti esauriti o dati non validi.';
            }
        } else if (response.status === 409) {
            msg.style.color = 'red';
            msg.textContent = 'Hai già prenotato questo evento.';
        } else {
            const err = await response.json().catch(() => null);
            msg.style.color = 'red';
            msg.textContent = err?.message || 'Errore durante la prenotazione.';
        }
    } catch (error) {
        console.error('Errore di connessione:', error);
    }
}