package com.academy.eventhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.eventhub.entity.Venue;

public interface VenueRepository extends JpaRepository<Venue,Integer> {

}
