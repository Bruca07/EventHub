package com.academy.eventhub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.eventhub.dto.UserRequestDTO;
import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.service.UserService;

import org.springframework.web.bind.annotation.RequestBody;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    UserService service;


       @PostMapping("/signup")

public ResponseEntity<UserResponseDTO> save(@RequestBody UserRequestDTO dto){

    return ResponseEntity.ok(service.register(dto));

}
}
