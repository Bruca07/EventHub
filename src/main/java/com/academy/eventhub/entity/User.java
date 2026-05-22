package com.academy.eventhub.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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


 @OneToMany(mappedBy = "user")
  List<Event> events;

  @OneToMany(mappedBy = "user")
  List<Ticket> tickets;

  @OneToMany(mappedBy = "user")
  List<FeedBack> feedBacks;
}
