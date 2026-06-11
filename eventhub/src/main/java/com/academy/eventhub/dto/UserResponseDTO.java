package com.academy.eventhub.dto;

import com.academy.eventhub.entity.Role;


import lombok.Data;

@Data
public class UserResponseDTO {
    
private int id;
private String username; 
private String email; 
private boolean enabled;
private Role role;
}
