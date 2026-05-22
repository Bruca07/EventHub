package com.academy.eventhub.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import lombok.Data;
@Entity
@Data
public class Speaker {
 @Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String firstName;
private String lastName;
private String company;

@ManyToMany(mappedBy = "speakers") 
private List<Event> events;
}