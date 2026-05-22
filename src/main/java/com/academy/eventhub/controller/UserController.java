package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.dto.UserRequestDTO;
import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.service.UserService;

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
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Endpoint per l'anagrafica degli utenti, registrazione di nuovi account e gestione dei ruoli di sicurezza.")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    @Operation(summary = "Recupera tutti gli utenti", description = "Restituisce la lista completa di tutti gli utenti registrati nel sistema (risposta in formato DTO di risposta sicuro).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista degli utenti recuperata con successo")
    })
    public List<UserResponseDTO> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova utente per ID", description = "Cerca un utente specifico tramite il suo identificativo univoco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utente trovato con successo"),
        @ApiResponse(responseCode = "404", description = "User non trovato")
    })
    public ResponseEntity<Object> findById(@PathVariable int id) {
        UserResponseDTO dto = service.findById(id);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }
        return ResponseEntity.status(404).body("User non trovato");
    }

    @PostMapping("/register")
    @Operation(summary = "Registra un nuovo utente", description = "Crea un nuovo account utente crittografando la password e assegnando i ruoli di base.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Utente registrato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati di registrazione non validi o username/email già esistenti")
    })
    public ResponseEntity<UserResponseDTO> save(@Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO createdUser = service.register(dto);
        return ResponseEntity.status(201).body(createdUser);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna dati utente", description = "Modifica le informazioni anagrafiche o le credenziali di un utente specifico tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utente aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    public ResponseEntity<UserResponseDTO> update(@PathVariable int id, @Valid @RequestBody UserRequestDTO dto) {
        UserResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un utente", description = "Rimuove definitivamente un utente dal database tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Utente eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Cambia il ruolo di un utente", description = "Aggiorna i privilegi o il ruolo (es. ADMIN, USER) di un utente specifico tramite parametro di query.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ruolo dell'utente aggiornato con successo"),
        @ApiResponse(responseCode = "400", description = "Nome del ruolo non valido"),
        @ApiResponse(responseCode = "404", description = "Utente non trovato")
    })
    public ResponseEntity<UserResponseDTO> changeRole(@PathVariable int id, @Valid @RequestParam String name){
        UserResponseDTO updatedUser = service.changeRole(id, name);
        return ResponseEntity.ok(updatedUser);
    }
}