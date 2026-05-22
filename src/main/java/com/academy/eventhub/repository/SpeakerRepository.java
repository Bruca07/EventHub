package com.academy.eventhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.eventhub.entity.Speaker;

public interface SpeakerRepository extends JpaRepository<Speaker, Integer> {

}
