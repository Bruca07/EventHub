package com.academy.eventhub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class TagDTO {
    
    @NotNull(message = "id obbligatorio")
    private int id;

    @NotBlank(message = "name obbligatorio") 
    private String name;
}
