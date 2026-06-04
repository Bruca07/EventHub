package com.academy.eventhub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
public class FeedBack {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String comment;
    @Min(1)
    @Max(5)
    private int rating;

    @ManyToOne
 @JoinColumn(name="user_id")
 private User user;

 @ManyToOne
 @JoinColumn(name="event_id")
 private Event event;

}
