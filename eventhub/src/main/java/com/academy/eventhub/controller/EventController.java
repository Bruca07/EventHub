package com.academy.eventhub.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Sort;

import com.academy.eventhub.dto.EventDTO;
import com.academy.eventhub.dto.TicketDTO;
import com.academy.eventhub.service.EventService;
import com.academy.eventhub.service.TicketService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Event Controller", description = "Endpoint per la ricerca, creazione, modifica ed eliminazione degli eventi, inclusi prenotazioni e rating.")
public class EventController {

    @Autowired 
    private EventService service;

    @Autowired
    private TicketService ticketService;

    @GetMapping
    @Operation(summary = "Recupera tutti gli eventi", description = "Restituisce una pagina di eventi con supporto alla paginazione e all'ordinamento di default per data.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pagina degli eventi recuperata con successo")
    })
    public Page<EventDTO> findAll(
        @PageableDefault(page = 0, size = 10, sort = "date", direction = Sort.Direction.ASC) Pageable p
    ) {
        return service.findAll(p);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova evento per ID", description = "Restituisce un singolo evento cercandolo tramite il suo identificativo univoco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento trovato con successo"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<EventDTO> findById(@PathVariable int id) {
        EventDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crea un nuovo evento", description = "Crea un evento nel sistema passando i dati necessari nel corpo della richiesta.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Evento creato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi")
    })
    public ResponseEntity<EventDTO> save(@Valid @RequestBody EventDTO dto) {
        EventDTO created = service.save(dto);
        return ResponseEntity.status(201).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna un evento esistente", description = "Modifica i dettagli di un evento specifico identificato dal suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evento aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<EventDTO> update(@PathVariable int id, @Valid @RequestBody EventDTO dto) {
        EventDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un evento", description = "Rimuove definitivamente un evento dal database tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Evento eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<Void> deleteById(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date")
    @Operation(summary = "Cerca eventi per data", description = "Filtra la lista degli eventi in base a una data specifica passata come parametro.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista eventi recuperata correttamente")
    })
    public List<EventDTO> getEventsByDate(@RequestParam LocalDate date) {
        return service.getEventsByDate(date);
    }

    @GetMapping("/venue")
    @Operation(summary = "Cerca eventi per nome della sede", description = "Filtra gli eventi in base al nome del luogo in cui si tengono.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista eventi recuperata correttamente")
    })
    public List<EventDTO> getEventsByVenueName(@RequestParam String name) {
        return service.getEventsByVenueName(name);
    }

    @GetMapping("/username")
    @Operation(summary = "Cerca eventi per username dell'utente", description = "Recupera la lista degli eventi associati o organizzati da un determinato username utente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista eventi recuperata correttamente")
    })
    public List<EventDTO> getEventsByUserUsername(@RequestParam String username) {
        return service.getEventsByUserUsername(username);
    } 

    @GetMapping("/TagsName")
    @Operation(summary = "Cerca eventi per nome del tag", description = "Filtra la lista degli eventi in base al nome di un tag/categoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista eventi recuperata correttamente")
    })
    public List<EventDTO> getEventsByTagsName(@RequestParam String name) {
        return service.getEventsByTagsName(name);
    }

    @PostMapping("/{id}/book")
    @Operation(summary = "Prenota un biglietto per l'evento", description = "Consente a un utente di acquistare o prenotare un biglietto per un evento specifico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Biglietto prenotato con successo"),
        @ApiResponse(responseCode = "400", description = "Posti esauriti o dati non validi"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<TicketDTO> bookEvent(@PathVariable int id, @Valid @RequestBody TicketDTO dto) {
        TicketDTO bookedTicket = ticketService.bookEvent(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookedTicket);
    }

    @GetMapping("/{id}/rating")
    @Operation(summary = "Calcola la media voto dell'evento", description = "Restituisce un numero intero da 1 a 5 che rappresenta la media aritmetica delle recensioni dell'evento.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Media voto calcolata con successo"),
        @ApiResponse(responseCode = "404", description = "Evento non trovato")
    })
    public ResponseEntity<Integer> getEventRating(@PathVariable int id) {
        int rating = service.getEventRating(id);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/city")
@Operation(summary = "Cerca eventi per città")
public List<EventDTO> getEventsByCity(@RequestParam String city) {
    return service.getEventsByCity(city);
}
}