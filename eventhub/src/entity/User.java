package com.academy.eventhub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
@Table(name = "users")
public class User {
@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String username;
private String password;
private boolean enabled; 

@ManyToOne
 @JoinColumn(name="role_id")
  private Role role;

@OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name="profile_id")
 private Profile profile;
}
