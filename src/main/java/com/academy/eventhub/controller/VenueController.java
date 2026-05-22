package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.dto.VenueDTO;
import com.academy.eventhub.service.VenueService;

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
@RequestMapping("/api/venues")
@Tag(name = "Venue Controller", description = "Endpoint per la gestione delle sedi, dei luoghi e delle location fisiche in cui si tengono gli eventi.")
public class VenueController {

    @Autowired 
    private VenueService service;

    @GetMapping
    @Operation(summary = "Recupera tutte le sedi", description = "Restituisce l'elenco completo di tutte le location e sedi registrate nel sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista delle sedi recuperata con successo")
    })
    public List<VenueDTO> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova sede per ID", description = "Restituisce i dettagli di una singola location cercandola tramite il suo identificativo univoco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sede trovata con successo"),
        @ApiResponse(responseCode = "404", description = "Sede non trovata")
    })
    public ResponseEntity<VenueDTO> findById(@PathVariable int id){
        VenueDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Crea una nuova sede", description = "Inserisce una nuova location all'interno del sistema passando i dati geometrici o di indirizzo nel corpo della richiesta.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "211", description = "Sede creata con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi")
    })
    public ResponseEntity<VenueDTO> save(@Valid @RequestBody VenueDTO dto){
        VenueDTO creaVenue = service.save(dto);
        return ResponseEntity.status(201).body(creaVenue);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna una sede esistente", description = "Modifica le informazioni (capacità, indirizzo, nome) di una specifica location tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sede aggiornata con successo"),
        @ApiResponse(responseCode = "400", description = "Dati inseriti non validi"),
        @ApiResponse(responseCode = "404", description = "Sede non trovata")
    })
    public ResponseEntity<VenueDTO> update(@PathVariable int id, @Valid @RequestBody VenueDTO dto){
        VenueDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una sede", description = "Rimuove definitivamente una location dal database tramite il suo ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Sede eliminata con successo"),
        @ApiResponse(responseCode = "404", description = "Sede non trovata")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}