package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.dto.TicketDTO;
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

    @GetMapping("/my")
@Operation(summary = "Recupera i ticket dell'utente loggato")
public ResponseEntity<List<TicketDTO>> getMyTickets(Authentication authentication) {
    String username = authentication.getName();
    List<TicketDTO> tickets = service.getTicketsByUsername(username);
    return ResponseEntity.ok(tickets);
}

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

    @GetMapping("/event/{eventId}")
@Operation(summary = "Recupera i ticket di un evento")
public ResponseEntity<List<TicketDTO>> getTicketsByEvent(@PathVariable int eventId) {
    List<TicketDTO> tickets = service.getTicketsByEventId(eventId);
    return ResponseEntity.ok(tickets);
}
}