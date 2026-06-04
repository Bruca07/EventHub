package com.academy.eventhub.entity;




import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Venue {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String name;
private String address;
private String city;
private int capacity;

@OneToMany(mappedBy = "venue")
private List<Event> events;


}
