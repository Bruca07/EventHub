package com.academy.eventhub.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; // <-- IMPORT CORRETTO
import org.springframework.security.core.context.SecurityContextHolder; // <-- MANCAVA QUESTO
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.TicketDTO;
import com.academy.eventhub.entity.Event;  // <-- IMPORT CORRETTO (la tua entità)
import com.academy.eventhub.entity.Ticket;
import com.academy.eventhub.entity.User;    // <-- MANCAVA QUESTO
import com.academy.eventhub.exception.ResourceNotFoundException; // <-- MANCAVA QUESTO
import com.academy.eventhub.mapper.TicketMapper;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.TicketRepository;
import com.academy.eventhub.repository.UserRepository;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepo;

    @Autowired
    TicketMapper ticketMapper;

    @Autowired
    EventRepository eventRepo;

    @Autowired
    UserRepository userRepo;

    public TicketDTO bookEvent(int id,TicketDTO dto) {
        // 1. Cerchiamo l'evento nel database usando l'id ricevuto
        Event event = eventRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            if (event.getDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Non è possibile prenotare un evento passato.");
    }

        // 2. Recuperiamo l'utente che sta effettuando la prenotazione
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User loggedInUser = userRepo.findByUsername(username);
        if (loggedInUser == null) {
            throw new ResourceNotFoundException("User not found");
        }
        if (ticketRepo.existsByEventAndUser(event, loggedInUser)) {
    throw new IllegalArgumentException("Hai già prenotato un biglietto per questo evento!");
}

        // 3. Creiamo il nuovo biglietto (Ticket) compilando i dati
        Ticket ticket = new Ticket();
        ticket.setStatus("ACTIVE");  
        ticket.setType(dto.getType());   
        ticket.setEvent(event);         
        ticket.setUser(loggedInUser);   

        // 4. Salviamo il biglietto nel database
        Ticket savedTicket = ticketRepo.save(ticket);

        // 5. Trasformiamo il ticket salvato in un TicketDTO e lo restituiamo
        TicketDTO savedDto = ticketMapper.toDTO(savedTicket);
        
        return savedDto;
    }

    public void delete(int id) {
    if (!ticketRepo.existsById(id)) {
        throw new ResourceNotFoundException("Ticket not found with id: " + id);
    }
    ticketRepo.deleteById(id);
}
}