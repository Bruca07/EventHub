# EventHub 🚀

EventHub è una piattaforma backend sviluppata con Spring Boot per la gestione e l'organizzazione di eventi.

---

## 🛠️ Prerequisiti

Prima di avviare il progetto, assicurati di aver installato sul tuo computer:
* **Java 21** (JDK)
* **Maven 3.x** (o utilizza il wrapper `mvnw` incluso)
* **Docker** e **Docker Desktop**
* Un client per testare le API (es. **Postman** o **Swagger UI**)

---

## 💾 Configurazione Database

Il progetto utilizza **PostgreSQL** tramite Docker:
* **Database Name:** `eventhub_db`
* **Username:** `academy_user`
* **Password:** `academy_password`
* **Porta:** `5432`

**Adminer** (interfaccia web per il database) è disponibile su `http://localhost:8090`

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

# Avvia l'applicazione
./mvnw spring-boot:run
```

---

## 📋 API Documentation

Swagger UI disponibile su: `http://localhost:8080/swagger-ui.html`

---

## 👤 Credenziali Demo

| Ruolo | Username | Password |
|-------|----------|----------|
| ADMIN | admin | admin123 |
| ORGANIZER | organizer | org123 |
| USER | user | user123 |