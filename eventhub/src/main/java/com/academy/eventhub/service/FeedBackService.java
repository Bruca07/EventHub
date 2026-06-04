package com.academy.eventhub.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.FeedBackDTO;
import com.academy.eventhub.entity.Event;
import com.academy.eventhub.entity.FeedBack;
import com.academy.eventhub.entity.Ticket;
import com.academy.eventhub.entity.User;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.FeedBackMapper;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.FeedbackRepository;
import com.academy.eventhub.repository.TicketRepository;
import com.academy.eventhub.repository.UserRepository;

@Service
public class FeedBackService {

    @Autowired
    FeedbackRepository feedbackRepo;

    @Autowired
    FeedBackMapper mapper;

    @Autowired
    UserRepository userRepo;  

    @Autowired
    EventRepository eventRepo;

    @Autowired
    TicketRepository ticketRepo;

    public List<FeedBackDTO> findAll(){
        return mapper.toDTOList(feedbackRepo.findAll());
    }

    public FeedBackDTO findById(int id){
        FeedBack feedBack = feedbackRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("FeedBack not found"));
        return mapper.toDTO(feedBack);
    }

    public FeedBackDTO save(FeedBackDTO dto) {
    FeedBack feedBack = mapper.toEntity(dto);
    
    User user = userRepo.findById(dto.getUserId())
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
    Event event = eventRepo.findById(dto.getEventId())
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
            
    //Controllo Data Evento
    if (event.getDate().isAfter(LocalDate.now())) {
        throw new IllegalArgumentException("Non è possibile dare un feedback di un evento non ancora concluso.");
    }
           
    //Controllo Esistenza Ticket
    Ticket ticket = ticketRepo.findByUserIdAndEventId(dto.getUserId(), dto.getEventId());
    if (ticket == null) {
        throw new IllegalArgumentException("Non è possibile lasciare un feedback: l'utente non ha acquistato un biglietto per questo evento.");
    }
    
    //Controllo Stato Ticket
    if (!ticket.getStatus().toString().equals("ACTIVE")) { 
        throw new IllegalArgumentException("Non è possibile dare un feedback, il biglietto non è valido.");
    }
    
    //Controllo Doppie Recensioni 
    for (FeedBack f : user.getFeedBacks()) {
        if (f.getEvent().getId() == dto.getEventId()) {
            throw new IllegalArgumentException("Hai già lasciato un feedback per questo evento! Non puoi lasciarne un altro.");
        }
    }

    
    feedBack.setUser(user);
    feedBack.setEvent(event);

    FeedBack saved = feedbackRepo.save(feedBack);
    return mapper.toDTO(saved);
} 

    public FeedBackDTO update(int id, FeedBackDTO dto){
        FeedBack feedBack = feedbackRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedBack not found"));
        
        feedBack.setComment(dto.getComment()); 
        feedBack.setRating(dto.getRating());

        User user = userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Event event = eventRepo.findById(dto.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        
        feedBack.setUser(user);
        feedBack.setEvent(event);

        FeedBack updated = feedbackRepo.save(feedBack);
        return mapper.toDTO(updated);  
    }


    public void deleteById(int id){
        feedbackRepo.deleteById(id);
    

}

public List<FeedBackDTO> findByEventId(int eventId) {
    return mapper.toDTOList(feedbackRepo.findByEventId(eventId));
}

}
