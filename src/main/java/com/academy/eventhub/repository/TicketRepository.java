package com.academy.eventhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.eventhub.entity.Event;
import com.academy.eventhub.entity.Ticket;
import com.academy.eventhub.entity.User;

public interface TicketRepository extends JpaRepository<Ticket,Integer> {

   boolean existsByEventAndUser(Event event, User user);

    Ticket findByUserIdAndEventId(Integer userId, Integer eventId);

}
