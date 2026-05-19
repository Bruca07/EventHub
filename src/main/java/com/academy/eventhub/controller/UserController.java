package com.academy.eventhub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;





@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService service;

    @GetMapping
    public List<UserResponseDTO> findAll(){
        return service.findAll();
    }


    }
    


    
    
    

