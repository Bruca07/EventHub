package com.academy.eventhub.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequestDTO {

@NotBlank(message = "Username obbligatorio")    
private String username;

@NotBlank(message = "Password obbligatoria")
private String password;

@Email(message = "Email non valida")
@NotBlank(message = "Email obbligatoria")
private String email;


}
