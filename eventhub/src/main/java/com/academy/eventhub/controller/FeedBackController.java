package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.academy.eventhub.dto.FeedBackDTO;
import com.academy.eventhub.service.FeedBackService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/feedBacks")
@Tag(name = "Feedback Controller", description = "Endpoint per la gestione delle recensioni e dei commenti lasciati dagli utenti sugli eventi.")
public class FeedBackController {

    @Autowired
    private FeedBackService service;

    @GetMapping 
    @Operation(summary = "Recupera tutti i feedback", description = "Restituisce la lista completa di tutti i feedback presenti nel database.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista dei feedback recuperata con successo")
    })
    public List<FeedBackDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova feedback per ID", description = "Restituisce una singola recensione cercandola tramite il suo identificativo univoco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback trovato con successo"),
        @ApiResponse(responseCode = "404", description = "Feedback non trovato")
    })
    public ResponseEntity<FeedBackDTO> findById(@PathVariable int id) {
        FeedBackDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(
        summary = "Lascia un nuovo feedback", 
        description = "Crea una nuova recensione. Include i controlli di validazione: l'evento deve essere concluso, l'utente deve avere un ticket valido e non può lasciare recensioni doppie."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Feedback salvato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati non validi o violazione dei vincoli di business (es. evento non concluso, ticket non valido, feedback duplicato)"),
        @ApiResponse(responseCode = "404", description = "Utente o Evento non trovato")
    })
    public ResponseEntity<FeedBackDTO> save(@Valid @RequestBody FeedBackDTO dto) {
        FeedBackDTO createFeedBack = service.save(dto);
        return ResponseEntity.status(201).body(createFeedBack);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna un feedback esistente", description = "Modifica il voto o il commento di una recensione specifica identificata dal suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feedback aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi"),
        @ApiResponse(responseCode = "404", description = "Feedback non trovato")
    })
    public ResponseEntity<FeedBackDTO> update(@PathVariable int id, @Valid @RequestBody FeedBackDTO dto) {
        FeedBackDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un feedback", description = "Rimuove definitivamente una recensione dal database tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Feedback eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Feedback non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/event/{eventId}")
@Operation(summary = "Recupera feedback per evento")
public List<FeedBackDTO> findByEventId(@PathVariable int eventId) {
    return service.findByEventId(eventId);
}
}