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
public class Tag {
@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String name;

@ManyToMany(mappedBy = "tags")
private List<Event> events;
}
