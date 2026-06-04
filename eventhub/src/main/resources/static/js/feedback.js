document.addEventListener('DOMContentLoaded', () => {

    // Logout
    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            sessionStorage.clear();
            window.location.href = 'login.html';
        });
    }

    const params = new URLSearchParams(window.location.search);
    const eventId = params.get('eventId');

    if (!eventId) {
        document.getElementById('form-feedback').innerHTML = '<p>Evento non trovato.</p>';
        return;
    }

    // Carica info evento
    loadEventInfo(eventId);

    // Bottone invia
    document.getElementById('btn-invia').addEventListener('click', () => inviaFeedback(eventId));
});

async function loadEventInfo(eventId) {
    const authHeader = sessionStorage.getItem('authHeader');

    try {
        const response = await fetch(`/api/events/${eventId}`, {
            headers: {
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        if (response.ok) {
            const event = await response.json();
            document.getElementById('evento-info').innerHTML = `
                <p><strong>Evento:</strong> ${event.title}</p>
                <p><strong>Data:</strong> ${event.date}</p>
            `;
        }
    } catch (error) {
        console.error('Errore:', error);
    }
}

async function inviaFeedback(eventId) {
    const authHeader = sessionStorage.getItem('authHeader');
    const msg = document.getElementById('msg-feedback');

    if (!authHeader) {
        window.location.href = 'login.html';
        return;
    }

    // Recupera userId dalla lista utenti
    const username = sessionStorage.getItem('username');
    const usersResp = await fetch('/api/users', {
        headers: {
            'Authorization': authHeader,
            'X-Requested-With': 'XMLHttpRequest'
        }
    });
    const users = await usersResp.json();
    const currentUser = users.find(u => u.username === username);

    if (!currentUser) {
        msg.style.color = 'red';
        msg.textContent = 'Errore utente non trovato.';
        return;
    }

    const dto = {
        eventId: parseInt(eventId),
        userId: currentUser.id,
        rating: parseInt(document.getElementById('rating').value),
        comment: document.getElementById('comment').value.trim()
    };

    if (!dto.comment) {
        msg.style.color = 'red';
        msg.textContent = 'Inserisci un commento.';
        return;
    }

    try {
        const response = await fetch('/api/feedBacks', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            msg.style.color = 'green';
            msg.textContent = 'Feedback inviato con successo!';
            document.getElementById('btn-invia').disabled = true;
        } else {
            const err = await response.json().catch(() => null);
            msg.style.color = 'red';
            msg.textContent = err?.message || 'Errore durante l\'invio del feedback.';
        }
    } catch (error) {
        console.error('Errore:', error);
    }
}