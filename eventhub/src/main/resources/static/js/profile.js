document.addEventListener('DOMContentLoaded', () => {

    const logoutLink = document.getElementById('logout-link');
    if (logoutLink) {
        logoutLink.addEventListener('click', (e) => {
            e.preventDefault();
            sessionStorage.clear();
            window.location.href = 'login.html';
        });
    }

    const authHeader = sessionStorage.getItem('authHeader');
    if (!authHeader) {
        window.location.href = 'login.html';
        return;
    }

    loadProfilo();

    document.getElementById('btn-salva-profilo').addEventListener('click', salvaProfilo);
});

let profiloId = null;

async function loadProfilo() {
    const authHeader = sessionStorage.getItem('authHeader');

    try {
        const response = await fetch('/api/profiles/me', {
            headers: {
                'Authorization': authHeader,
                'X-Requested-With': 'XMLHttpRequest'
            }
        });

        if (response.ok) {
            const profilo = await response.json();
            profiloId = profilo.id;

            document.getElementById('profilo-info').innerHTML = `
                <p><strong>Nome:</strong> ${profilo.firstName || 'N/D'}</p>
                <p><strong>Cognome:</strong> ${profilo.lastName || 'N/D'}</p>
                <p><strong>Bio:</strong> ${profilo.bio || 'N/D'}</p>
                <p><strong>Città:</strong> ${profilo.city || 'N/D'}</p>
            `;

            document.getElementById('input-firstname').value = profilo.firstName || '';
            document.getElementById('input-lastname').value = profilo.lastName || '';
            document.getElementById('input-bio').value = profilo.bio || '';
            document.getElementById('input-city').value = profilo.city || '';
            document.getElementById('input-photo').value = profilo.photo || '';

        } else if (response.status === 404) {
            document.getElementById('profilo-info').innerHTML = '<p>Profilo non ancora creato.</p>';
        }
    } catch (error) {
        console.error('Errore caricamento profilo:', error);
    }
}

async function salvaProfilo() {
    const authHeader = sessionStorage.getItem('authHeader');
    const msg = document.getElementById('msg-profilo');

    const dto = {
        firstName: document.getElementById('input-firstname').value.trim(),
        lastName: document.getElementById('input-lastname').value.trim(),
        bio: document.getElementById('input-bio').value.trim(),
        city: document.getElementById('input-city').value.trim(),
        photo: document.getElementById('input-photo').value.trim()
    };

    const url = profiloId ? `/api/profiles/${profiloId}` : '/api/profiles';
    const method = profiloId ? 'PUT' : 'POST';

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
            msg.textContent = 'Profilo salvato con successo!';
            loadProfilo();
        } else {
            msg.style.color = 'red';
            msg.textContent = 'Errore durante il salvataggio.';
        }
    } catch (error) {
        console.error(error);
    }
}