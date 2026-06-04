package com.academy.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.academy.eventhub.dto.TicketDTO;
import com.academy.eventhub.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @Test
    @WithMockUser(username = "user_demo")
    void testBookEvent_DovrebbeLanciareEccezioneSeGiaPrenotato() {

        TicketDTO ticket = new TicketDTO();
        ticket.setEventId(1);
        ticket.setUsername("user_demo");

        
        ticketService.bookEvent(1, ticket);

        
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.bookEvent(1, ticket);
        });

       
        assertThrows(ResourceNotFoundException.class, () -> {
            ticketService.bookEvent(999, ticket);
        });

        
        assertThrows(IllegalArgumentException.class, () -> {
            ticketService.bookEvent(7, ticket);
        });
    }
}