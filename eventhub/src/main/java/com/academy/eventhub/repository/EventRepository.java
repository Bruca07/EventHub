package com.academy.eventhub.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.eventhub.entity.Event;

public interface EventRepository extends JpaRepository<Event,Integer> {

    List<Event> findByDate(LocalDate date);

    List<Event> findByVenueName(String name);

    List<Event> findByUserUsername(String username);

    List<Event> findByTagsName(String name);

    List<Event> findByVenueCity(String city);
}
