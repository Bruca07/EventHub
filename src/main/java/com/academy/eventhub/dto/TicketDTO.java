package com.academy.eventhub.dto;


import com.academy.eventhub.entity.TicketType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TicketDTO {
    private int id;
    
    @NotBlank(message = "status obbligatorio")
    private String status;
    
    @NotNull(message = "eventId obbligatorio")
    private Integer eventId;
    
    @NotBlank(message = "username obbligatorio")
    private String username;

    @NotNull(message = "Il tipo di biglietto è obbligatorio") 
    private TicketType type;
}
