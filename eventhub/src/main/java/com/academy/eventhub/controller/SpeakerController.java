package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.dto.SpeakerDTO;
import com.academy.eventhub.service.SpeakerService;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/speakers")
@Tag(name = "Speaker Controller", description = "Endpoint per la gestione degli speaker (ospiti/relatori) partecipanti agli eventi.")
public class SpeakerController {

    @Autowired 
    private SpeakerService service;

    @GetMapping
    @Operation(summary = "Recupera tutti gli speaker", description = "Restituisce l'elenco completo di tutti i relatori e ospiti censiti nel sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista degli speaker recuperata con successo")
    })
    public List<SpeakerDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova speaker per ID", description = "Restituisce i dettagli di un singolo relatore cercandolo tramite il suo identificativo univoco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Speaker trovato con successo"),
        @ApiResponse(responseCode = "404", description = "Speaker non trovato")
    })
    public ResponseEntity<SpeakerDTO> findById(@PathVariable int id) {
        SpeakerDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crea un nuovo speaker", description = "Registra un nuovo relatore all'interno del sistema passando i dati richiesti nel corpo della richiesta.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Speaker registrato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi")
    })
    public ResponseEntity<SpeakerDTO> save(@Valid @RequestBody SpeakerDTO dto) {
        SpeakerDTO createSpeaker = service.save(dto);
        return ResponseEntity.status(201).body(createSpeaker);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna uno speaker esistente", description = "Modifica le informazioni anagrafiche o professionali di uno speaker specifico tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Speaker aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi"),
        @ApiResponse(responseCode = "404", description = "Speaker non trovato")
    })
    public ResponseEntity<SpeakerDTO> update(@PathVariable int id, @Valid @RequestBody SpeakerDTO dto) {
        SpeakerDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina uno speaker", description = "Rimuove definitivamente l'anagrafica di uno speaker dal database tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Speaker eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Speaker non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}