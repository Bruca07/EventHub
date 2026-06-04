package com.academy.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDTO { 
private Integer id;
    
@NotBlank(message = "firstName obbligatorio")      
private String firstName;

@NotBlank(message = "lastName obbligatorio")  
private String lastName;

@NotBlank(message = "bio obbligatorio")  
private String bio;

@NotBlank(message = "city obbligatorio")  
private String city;

  
private String photo;

}
