package com.academy.eventhub.dto;

import com.academy.eventhub.entity.Role;


import lombok.Data;

@Data
public class UserResponseDTO {
    
private int id;
private String username;  
private boolean enabled;
private Role role;
}
