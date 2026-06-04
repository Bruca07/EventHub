package com.academy.eventhub.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedBackDTO {
@NotBlank(message = "comment obbligatorio")
private String comment;

@NotNull(message = "eventId obbligatorio")
private Integer eventId;

@NotNull(message = "userId obbligatorio")
private Integer userId;

@Min(1)
@Max(5)
private int rating;

}
