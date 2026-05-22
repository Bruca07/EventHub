package com.academy.eventhub.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EventDTO {
@NotBlank(message = "title obbligatorio")
private String title;

@NotBlank(message = "description obbligatorio")
private String description;

@NotNull(message = "date obbligatoria")
private LocalDate date;

@NotNull(message = "maxSeats obbligatoria")
private Integer maxSeats;


    private Integer venueId;
    private Integer userId;
    private List<Integer> tagIds;
    private int availableSeats;
    private List<Integer> speakerIds;

}

