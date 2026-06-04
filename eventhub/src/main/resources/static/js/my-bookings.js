document.addEventListener('DOMContentLoaded', () => {
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            sessionStorage.clear();
            window.location.href = 'login.html';
        });
    }

    loadMyBookings();
});

async function loadMyBookings() {
    const authHeader = sessionStorage.getItem('authHeader');
    const lista = document.getElementById('lista-bookings');
    const msg = document.getElementById('msg-bookings');

    if (!authHeader) {
        window.location.href = 'login.html';
        return;
    }

    try {
        const response = await fetch('/api/tickets/my', {
            headers: {
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        if (response.ok) {
            const tickets = await response.json();

            if (tickets.length === 0) {
                msg.textContent = 'Non hai ancora prenotato alcun evento.';
                return;
            }

            lista.innerHTML = '';
            const oggi = new Date().toISOString().split('T')[0];

            for (const ticket of tickets) {
                const eventResp = await fetch(`/api/events/${ticket.eventId}`, {
                    headers: {
                        'Authorization': authHeader,
                        'X-Requested-With': 'XMLHttpRequest'
                    }
                });

                let titolo = `Evento #${ticket.eventId}`;
                let data = '';
                let eventoPassato = false;

                if (eventResp.ok) {
                    const event = await eventResp.json();
                    titolo = event.title || titolo;
                    data = event.date || '';
                    eventoPassato = data < oggi;
                }

                const div = document.createElement('div');
                div.className = 'event-card';
                div.innerHTML = `
                    <h3>${titolo}</h3>
                    <p><strong>Data:</strong> ${data}</p>
                    <p><strong>Tipo:</strong> ${ticket.type}</p>
                    <p><strong>Stato:</strong> ${eventoPassato ? '🔴 Concluso' : '🟢 Attivo'}</p>
                    ${eventoPassato 
                        ? `<button onclick="window.location.href='feedback.html?eventId=${ticket.eventId}'">Lascia un feedback</button>` 
                        : `<button onclick="cancellaPrenotazione(${ticket.id})">Cancella prenotazione</button>`
                    }
                `;
                lista.appendChild(div);
            }
        } else {
            msg.textContent = 'Errore nel recupero delle prenotazioni.';
        }
    } catch (error) {
        console.error('Errore:', error);
        msg.textContent = 'Errore di connessione al server.';
    }
}

async function cancellaPrenotazione(ticketId) {
    if (!confirm('Sei sicuro di voler cancellare questa prenotazione?')) return;

    const authHeader = sessionStorage.getItem('authHeader');

    try {
        const response = await fetch(`/api/tickets/${ticketId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        if (response.ok || response.status === 204) {
            alert('Prenotazione cancellata!');
            loadMyBookings();
        } else {
            alert('Errore durante la cancellazione.');
        }
    } catch (error) {
        console.error('Errore:', error);
    }
}