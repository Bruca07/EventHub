package com.academy.eventhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.academy.eventhub.dto.UserRequestDTO;
import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "Endpoint pubblici per l'autenticazione, la registrazione e l'accesso alla piattaforma.")
public class AuthController {

    @Autowired
    private UserService service;

    @PostMapping("/signup")
    @Operation(summary = "Registrazione nuovo utente (Signup)", description = "Permette a un ospite di registrarsi sulla piattaforma inserendo username, email e password.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Utente registrato con successo"),
        @ApiResponse(responseCode = "400", description = "Dati di registrazione non validi o credenziali già esistenti")
    })
    public ResponseEntity<UserResponseDTO> save(@RequestBody UserRequestDTO dto) {
        return ResponseEntity.ok(service.register(dto));
    }
}