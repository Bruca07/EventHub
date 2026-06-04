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
@Tag(name = "User Controller", description = "Endpoint per la gestione degli utenti.")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    @Operation(summary = "Recupera tutti gli utenti")
    public List<UserResponseDTO> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Trova utente per ID")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        UserResponseDTO dto = service.findById(id);
        if (dto != null) return ResponseEntity.ok(dto);
        return ResponseEntity.status(404).body("User non trovato");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un utente")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Cambia il ruolo di un utente")
    public ResponseEntity<UserResponseDTO> changeRole(@PathVariable int id, @RequestParam String name) {
        return ResponseEntity.ok(service.changeRole(id, name));
    }

    @PutMapping("/{id}/ban")
    @Operation(summary = "Ban/unban utente")
    public ResponseEntity<UserResponseDTO> toggleBan(@PathVariable int id) {
        return ResponseEntity.ok(service.toggleBan(id));
    }
}