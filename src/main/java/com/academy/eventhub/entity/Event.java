package com.academy.eventhub.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Event {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;

private String title;
private String description;
private LocalDate date;
private int maxSeats;

@ManyToOne
 @JoinColumn(name="venue_id")
 private Venue venue;

 @ManyToMany
@JoinTable(
    name = "event_tag",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
    private List<Tag> tags;

@ManyToOne
 @JoinColumn(name="user_id")
 private User user;

 @ManyToMany
@JoinTable(
    name = "event_speaker",
    joinColumns = @JoinColumn(name = "event_id"),
    inverseJoinColumns = @JoinColumn(name = "speaker_id")
)
private List<Speaker> speakers;

@OneToMany(mappedBy = "event")
  List<Ticket> tickets;

  @OneToMany(mappedBy = "event")
  List<FeedBack> feedBacks;

  

}
