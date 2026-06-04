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
import com.academy.eventhub.dto.TagDTO;
import com.academy.eventhub.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tag Controller", description = "Endpoint per la gestione delle categorie degli eventi.")
public class TagController {

    @Autowired
    private TagService service;

    @GetMapping
    @Operation(summary = "Recupera tutti i tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista tag recuperata con successo")
    })
    public List<TagDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova tag per ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag trovato"),
        @ApiResponse(responseCode = "404", description = "Tag non trovato")
    })
    public ResponseEntity<TagDTO> findById(@PathVariable int id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Crea un nuovo tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tag creato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati non validi")
    })
    public ResponseEntity<TagDTO> save(@Valid @RequestBody TagDTO dto) {
        return ResponseEntity.status(201).body(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna un tag esistente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag aggiornato"),
        @ApiResponse(responseCode = "404", description = "Tag non trovato")
    })
    public ResponseEntity<TagDTO> update(@PathVariable int id, @Valid @RequestBody TagDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un tag")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tag eliminato"),
        @ApiResponse(responseCode = "404", description = "Tag non trovato")
    })
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
