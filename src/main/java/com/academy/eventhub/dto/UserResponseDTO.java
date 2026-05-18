package com.academy.eventhub.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    
private int id;
private String username;
private boolean enabled;
private String roleName;
}
