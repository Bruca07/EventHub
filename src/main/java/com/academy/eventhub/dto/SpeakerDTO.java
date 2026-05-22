package com.academy.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SpeakerDTO {

@NotBlank(message = "firstName obbligatorio") 
private String firstName;

@NotBlank(message = "lastName obbligatorio") 
private String lastName;

@NotBlank(message = "company obbligatorio") 
private String company;
}
