# EventHub 🚀

EventHub è una piattaforma backend sviluppata con Spring Boot per la gestione e l'organizzazione di eventi.

---

## 🛠️ Prerequisiti

Prima di avviare il progetto, assicurati di aver installato sul tuo computer:
* **Java 17** o superiore (JDK)
* **Maven 3.x** (o utilizza il wrapper `mvnw` incluso)
* **Docker** e **Docker Desktop** (fondamentale per il database)
* Un client per testare le API (es. **Postman** o l'interfaccia integrata **Swagger UI**)

---

## 💾 Configurazione Database

Il progetto utilizza un database **PostgreSQL** configurato in ambiente isolato tramite Docker. Non è necessario installare PostgreSQL localmente sul proprio sistema operativo.

I dati di configurazione e le credenziali all'avvio sono i seguenti:
* **DBMS:** PostgreSQL 15 (Alpine)
* **Database Name:** `eventhub_user`
* **Username:** `academy_user`
* **Password:** `eventhub_pass`
* **Porta:** `5432`

Inoltre, è integrato **Adminer** come interfaccia web di gestione del database sulla porta `8080`.

---

## 🐋 Comandi Docker Principali

Tutta l'infrastruttura del database viene gestita tramite il file `docker-compose.yml`. Di seguito i comandi principali da lanciare nel terminale della cartella principale:

* **Avviare il database in background:**
  ```bash
  docker compose up -d