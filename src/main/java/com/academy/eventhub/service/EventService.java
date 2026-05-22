package com.academy.eventhub.service;



import java.time.LocalDate;
import java.util.List;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.academy.eventhub.dto.EventDTO;
import com.academy.eventhub.entity.Event;
import com.academy.eventhub.entity.FeedBack;
import com.academy.eventhub.entity.Ticket;
import com.academy.eventhub.entity.User;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.EventMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.UserRepository;



@Service
public class EventService {

    @Autowired
    EventRepository eventRepo;

    @Autowired
    EventMapper mapper;

    @Autowired
    UserRepository userRepo;

  public Page<EventDTO> findAll(Pageable p) {
    Page<Event> eventPage = eventRepo.findAll(p);
    Page<EventDTO> dtoPage = eventPage.map(event -> {
        EventDTO dto = mapper.toDTO(event); 
        populateAvailableSeats(event, dto); 
        return dto; 
    });
    
    return dtoPage;
}

  public EventDTO findById(int id) {
    Event event = eventRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    EventDTO dto = mapper.toDTO(event); 
    EventDTO updatedDto = populateAvailableSeats(event, dto);
    return updatedDto;
}


public EventDTO save(EventDTO dto) {
    Event event = mapper.toEntity(dto);   
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    User loggedInUser = userRepo.findByUsername(username);
    if (loggedInUser == null) {
        throw new ResourceNotFoundException("Utente non trovato");
    }
//Colleghiamo l'utente trovato come creatore dell'evento
    event.setUser(loggedInUser); 
    // 6. Salviamo l'evento completo sul database (restituisce l'entità aggiornata con l'ID)
    Event savedEvent = eventRepo.save(event);  
    // 7. Convertiamo l'evento salvato dal database in un DTO
    EventDTO savedDto = mapper.toDTO(savedEvent);   
    // 8. Calcoliamo i posti disponibili passando i dati freschi del database
    EventDTO updatedDto = populateAvailableSeats(savedEvent, savedDto);
    
    return updatedDto;
} 

public EventDTO update(int id, EventDTO dto) {
    // Cerchiamo l'evento esistente nel database. Se non c'è, errore 404.
    Event event = eventRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    
    // Recuperiamo chi sta provando a fare la modifica
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    
    // CONTROLLO DI SICUREZZA: se NON sei il proprietario, blocchiamo l'azione.
    if (!event.getUser().getUsername().equals(username)) {
        throw new AccessDeniedException("Non puoi modificare questo evento");
    }  
    // Se il controllo passa, aggiorniamo i campi dell'evento
    event.setTitle(dto.getTitle());
    event.setDescription(dto.getDescription());
    event.setDate(dto.getDate());
    event.setMaxSeats(dto.getMaxSeats());
    
    // Salviamo le modifiche nel database
    Event updateEvent = eventRepo.save(event);
   EventDTO savedDto = mapper.toDTO(updateEvent);
   EventDTO updatedDto = populateAvailableSeats(updateEvent, savedDto);
    // Restituiamo il DTO aggiornato
    return updatedDto;
} 

public void deleteById(int id) {
    eventRepo.deleteById(id);
} 

public List<EventDTO> getEventsByDate(LocalDate date) {
        // 1. Chiediamo al database la lista di entità Event filtrate per data
        List<Event> events = eventRepo.findByDate(date);      
        // 2. Trasformiamo la lista di entità in una lista di DTO usando il mapper
        List<EventDTO> dtos = mapper.toDTOList(events);
        for (int i = 0; i < events.size(); i++) {
        populateAvailableSeats(events.get(i), dtos.get(i));
    }
    return dtos;
    }

    public List<EventDTO> getEventsByVenueName(String name){
        List<Event> events = eventRepo.findByVenueName(name);
        List<EventDTO> dtos = mapper.toDTOList(events);
        for (int i = 0; i < events.size(); i++) {
            populateAvailableSeats(events.get(i), dtos.get(i));
        }
        return dtos;
    }

    public List<EventDTO> getEventsByUserUsername(String username){
        List<Event> events = eventRepo.findByUserUsername(username);
        List<EventDTO> dtos = mapper.toDTOList(events);
        for (int i = 0; i < events.size(); i++) {
            populateAvailableSeats(events.get(i), dtos.get(i));
        }
        return dtos;
    }

    public List<EventDTO> getEventsByTagsName(String name){
        List<Event> events = eventRepo.findByTagsName(name);
        List<EventDTO> dtos = mapper.toDTOList(events);
        for (int i = 0; i < events.size(); i++) {
            populateAvailableSeats(events.get(i), dtos.get(i));
        }
        return dtos;
    }

private EventDTO populateAvailableSeats(Event event, EventDTO dto) {
    int activeTickets = 0; 
    if (event.getTickets() != null) {
        for (Ticket t : event.getTickets()) {
            if ("ACTIVE".equals(t.getStatus())) {
                activeTickets++;
            }
        } 
    } 
    int available = event.getMaxSeats() - activeTickets;
    dto.setAvailableSeats(available);
    return dto;
 }

 public int getEventRating(int eventId) {
    Event event = eventRepo.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

    // Prendiamo la lista dei feedback di questo evento
    List<FeedBack> feedbacks = event.getFeedBacks();

    if (feedbacks.isEmpty()) {
        return 0;
    }
    // 3. Calcoliamo la media
    int sommaVoti = 0;
    for (FeedBack f : feedbacks) {
        sommaVoti += f.getRating(); // Sommiamo il voto di ogni feedback
    }

    // Dividiamo la somma per il numero totale di feedback per avere la media intera
    return sommaVoti / feedbacks.size();
}
}