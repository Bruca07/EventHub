package com.academy.eventhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.service.TicketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Controller", description = "Endpoint per la gestione dei biglietti e delle prenotazioni degli eventi.")
public class TicketController {

    @Autowired
    private TicketService service;

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina o annulla un ticket", description = "Rimuove definitivamente un biglietto/prenotazione dal database tramite il suo ID (es. in caso di disdetta).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ticket eliminato con successo"),
        @ApiResponse(responseCode = "404", description = "Ticket non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable int id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}