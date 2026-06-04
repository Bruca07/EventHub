package com.academy.eventhub.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication; 
import org.springframework.security.core.context.SecurityContextHolder; 
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.TicketDTO;
import com.academy.eventhub.entity.Event;  
import com.academy.eventhub.entity.Ticket;
import com.academy.eventhub.entity.User;   
import com.academy.eventhub.exception.ResourceNotFoundException; 
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
    long activeTickets = ticketRepo.countByEventAndStatus(event, "ACTIVE");
if (activeTickets >= event.getMaxSeats()) {
    throw new IllegalArgumentException("Posti esauriti!");
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
    Ticket ticket = ticketRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    if (ticket.getEvent().getDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Non puoi cancellare una prenotazione per un evento già iniziato.");
    }
    ticketRepo.deleteById(id);
}

public List<TicketDTO> getTicketsByUsername(String username) {
    List<Ticket> tickets = ticketRepo.findByUserUsername(username);
    List<TicketDTO> dtos = new ArrayList<>();
    
    for (Ticket ticket : tickets) {
        TicketDTO dto = ticketMapper.toDTO(ticket);
        dtos.add(dto);
    }
    
    return dtos;
}
public List<TicketDTO> getTicketsByEventId(int eventId) {
    List<Ticket> tickets = ticketRepo.findByEventId(eventId);
    List<TicketDTO> dtos = new ArrayList<>();
    for (Ticket ticket : tickets) {
        dtos.add(ticketMapper.toDTO(ticket));
    }
    return dtos;
}
}