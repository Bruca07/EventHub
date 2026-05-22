package com.academy.eventhub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Ticket {

@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private int price;
private String status;

@Enumerated(EnumType.STRING)
private TicketType type;

@ManyToOne
 @JoinColumn(name="user_id")
 private User user;

 @ManyToOne
 @JoinColumn(name="event_id")
 private Event event;
}
