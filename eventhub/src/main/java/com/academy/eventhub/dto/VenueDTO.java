package com.academy.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VenueDTO {
private Integer id;

@NotBlank(message = "name obbligatorio") 
private String name;

@NotBlank(message = "address obbligatorio") 
private String address;

@NotBlank(message = "city obbligatorio") 
private String city;


private int capacity;
}
