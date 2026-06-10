# EventHub 🚀

EventHub è una piattaforma sviluppata con Spring Boot per la gestione e l'organizzazione di eventi.

---

## 🛠️ Prerequisiti

* **Java 21** (JDK)
* **Maven 3.x** (o utilizza il wrapper `mvnw` incluso)
* **Docker** e **Docker Desktop**
* Un client per testare le API **Swagger UI**

---

## 💾 Configurazione Database

Il progetto utilizza **PostgreSQL** tramite Docker:
* **Database Name:** `eventhub_db`
* **Username:** `user`
* **Password:** `password`
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

🚀 Avvio del Progetto
# Clona il repository
git clone [https://github.com/Bruca07/EventHub.git](https://github.com/Bruca07/EventHub.git)
cd EventHub

# Avvia il database
docker compose up -d

# Avvia l'applicazione
./mvnw spring-boot:run
