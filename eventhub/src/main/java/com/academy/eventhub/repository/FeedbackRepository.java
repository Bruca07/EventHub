package com.academy.eventhub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.eventhub.entity.FeedBack;

public interface FeedbackRepository extends JpaRepository<FeedBack, Integer>{
    
List<FeedBack> findByEventId(int eventId);
}
