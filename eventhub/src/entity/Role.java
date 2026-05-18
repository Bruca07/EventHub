package com.academy.eventhub.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
@Data
public class Role {
 @Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
private String name;
}
