package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.dto.ProfileDTO;
import com.academy.eventhub.service.ProfileService;
import org.springframework.security.core.Authentication;
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
@RequestMapping("/api/profiles")
@Tag(name = "Profile Controller", description = "Endpoint per la gestione dei profili personali degli utenti, incluse le informazioni sull'utente correntemente autenticato.")
public class ProfileController {

    @Autowired
    private ProfileService service;

    @GetMapping
    @Operation(summary = "Recupera tutti i profili", description = "Restituisce la lista completa di tutti i profili utente registrati nel sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista dei profili recuperata con successo")
    })
    public List<ProfileDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova profilo per ID", description = "Restituisce i dettagli di un singolo profilo cercandolo tramite il suo identificativo univoco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profilo trovato con successo"),
        @ApiResponse(responseCode = "404", description = "Profilo non trovato")
    })
    public ResponseEntity<ProfileDTO> findById(@PathVariable int id) {
        ProfileDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crea un nuovo profilo", description = "Crea una scheda profilo associandola a un utente esistente inserendo i dati richiesti.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Profilo creato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi")
    })
    public ResponseEntity<ProfileDTO> save(@Valid @RequestBody ProfileDTO dto) {
        ProfileDTO createProfile = service.save(dto);
        return ResponseEntity.status(201).body(createProfile);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna un profilo esistente", description = "Modifica le informazioni di un profilo specifico identificato dal suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profilo aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi"),
        @ApiResponse(responseCode = "404", description = "Profilo non trovato")
    })
    public ResponseEntity<ProfileDTO> update(@PathVariable int id, @Valid @RequestBody ProfileDTO dto) {
        ProfileDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un profilo", description = "Rimuove definitivamente un profilo dal database tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Profilo eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Profilo non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    @Operation(summary = "Recupera il profilo dell'utente loggato", description = "Estrae il nome utente dal contesto di sicurezza (Authentication) e restituisce il relativo profilo personale.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profilo dell'utente corrente recuperato con successo"),
        @ApiResponse(responseCode = "401", description = "Non autorizzato - Token mancante o non valido"),
        @ApiResponse(responseCode = "404", description = "Profilo non associato all'utente loggato")
    })
    public ProfileDTO me(Authentication authentication) {
        String username = authentication.getName();
        return service.findByUserUsername(username);
    }
}