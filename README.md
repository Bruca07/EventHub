# EventHub 🚀

EventHub è una piattaforma sviluppata con Spring Boot per la gestione e l'organizzazione di eventi: concerti, spettacoli, workshop e molto altro.

---

## 🛠️ Prerequisiti

Prima di avviare il progetto, assicurati di aver installato sul tuo computer:
* **Java 21** (JDK)
* **Maven 3.x**
* **Docker** e **Docker Desktop**
* Un client per testare le API (es. **Postman** o **Swagger UI**)

---

## 💾 Configurazione Database

Il progetto utilizza **PostgreSQL** tramite Docker:
* **Database Name:** `eventhub_db`
* **Username:** `academy_user`
* **Password:** `academy_password`
* **Porta:** `5432`

**Adminer** è disponibile su `http://localhost:8090`

---

## 🐋 Comandi Docker Principali

```bash
# Avviare il database in background
docker compose up -d

# Fermare i container
docker compose down

# Vedere i log
docker compose logs
```

---

## 🚀 Avvio del Progetto

```bash
# Clona il repository
git clone https://github.com/Bruca07/EventHub.git
cd EventHub

# Avvia il database
docker compose up -d

# Entra nella cartella del progetto
cd eventhub

# Avvia l'applicazione
mvn spring-boot:run
```

L'applicazione sarà disponibile su `http://localhost:8080`

---

## 👤 Credenziali Demo

| Ruolo | Username | Password |
|-------|----------|----------|
| ADMIN | admin_demo | admin123 |
| ORGANIZER | organizer_demo | org123 |
| USER | user_demo | user123 |

---

## 📋 API Documentation

Swagger UI disponibile su: `http://localhost:8080/swagger-ui.html`

---

## ✨ Descrizione delle Funzionalità

### USER
- Registrazione e login
- Sfoglia il catalogo eventi con filtri per data, tag e città
- Visualizza il dettaglio evento con sede, relatori, prezzi e feedback
- Prenota un biglietto Standard o VIP
- Gestisce le proprie prenotazioni e le cancella prima dell'evento
- Lascia un feedback con voto e commento dopo l'evento
- Visualizza e modifica il proprio profilo

### ORGANIZER
- Crea, modifica ed elimina i propri eventi
- Associa sede, relatori, tag e prezzi all'evento
- Monitora i partecipanti e i feedback ricevuti

### ADMIN
- Promuove utenti a ORGANIZER o revoca il ruolo
- Banna o riattiva utenti
- Gestisce il catalogo sedi, relatori e categorie
- Modera i feedback inappropriati

---

## 🔒 Regole di Business

- Non si possono prenotare eventi passati
- Non si possono fare doppie prenotazioni per lo stesso evento
- Il feedback è consentito solo dopo l'evento e solo se si ha un ticket valido
- Non si può lasciare più di un feedback per evento
- La cancellazione della prenotazione è consentita solo prima dell'evento
- I posti disponibili vengono aggiornati in tempo reale

---

## 🗂️ Stack Tecnologico

* **Backend:** Java 21, Spring Boot 3.2, Spring Security, Spring Data JPA
* **Database:** PostgreSQL 15
* **Mapper:** MapStruct
* **Documentazione:** SpringDoc OpenAPI (Swagger)
* **Frontend:** HTML5, CSS3, JavaScript vanilla
* **Container:** Docker, Docker Compose