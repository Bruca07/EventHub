package com.academy.eventhub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Profile {
@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String firstName;
private String lastName;
private String bio;
private String city;
private String photo;

@OneToOne(mappedBy = "profile")
private User user;

}
