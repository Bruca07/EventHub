package com.academy.eventhub.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.EventDTO;
import com.academy.eventhub.entity.Event;
import com.academy.eventhub.entity.FeedBack;
import com.academy.eventhub.entity.Ticket;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.EventMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.SpeakerRepository;
import com.academy.eventhub.repository.TagRepository;
import com.academy.eventhub.repository.UserRepository;
import com.academy.eventhub.repository.VenueRepository;

@Service
public class EventService {

    @Autowired EventRepository eventRepo;
    @Autowired EventMapper mapper;
    @Autowired UserRepository userRepo;
    @Autowired VenueRepository venueRepo;
    @Autowired TagRepository tagRepo;
    @Autowired SpeakerRepository speakerRepo;

    public Page<EventDTO> findAll(Pageable p) {
        return eventRepo.findAll(p).map(event -> {
            EventDTO dto = mapper.toDTO(event);
            populateAvailableSeats(event, dto);
            populateSpeakersAndTags(event, dto);
            return dto;
        });
    }

    public EventDTO findById(int id) {
        Event event = eventRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        EventDTO dto = mapper.toDTO(event);
        populateAvailableSeats(event, dto);
        populateSpeakersAndTags(event, dto);
        return dto;
    }

    public EventDTO save(EventDTO dto) {
        Event event = mapper.toEntity(dto);
        associaRelazioni(event, dto);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        event.setUser(userRepo.findByUsername(auth.getName()));
        Event savedEvent = eventRepo.save(event);
        return toEventDTOWithDetails(savedEvent);
    }

    public EventDTO update(int id, EventDTO dto) {
        Event event = eventRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!event.getUser().getUsername().equals(auth.getName())) {
            throw new AccessDeniedException("Non puoi modificare questo evento");
        }

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setDate(dto.getDate());
        event.setMaxSeats(dto.getMaxSeats());
        associaRelazioni(event, dto);

        return toEventDTOWithDetails(eventRepo.save(event));
    }

    public void deleteById(int id) {
        eventRepo.deleteById(id);
    }

    public List<EventDTO> getEventsByDate(LocalDate date) {
        return toDTOsWithDetails(eventRepo.findByDate(date));
    }

    public List<EventDTO> getEventsByVenueName(String name) {
        return toDTOsWithDetails(eventRepo.findByVenueName(name));
    }

    public List<EventDTO> getEventsByUserUsername(String username) {
        return toDTOsWithDetails(eventRepo.findByUserUsername(username));
    }

    public List<EventDTO> getEventsByTagsName(String name) {
        return toDTOsWithDetails(eventRepo.findByTagsName(name));
    }

    public List<EventDTO> getEventsByCity(String city) {
        return toDTOsWithDetails(eventRepo.findByVenueCity(city));
    }

    public int getEventRating(int eventId) {
        Event event = eventRepo.findById(eventId)
            .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        List<FeedBack> feedbacks = event.getFeedBacks();
        if (feedbacks.isEmpty()) return 0;
        return feedbacks.stream().mapToInt(FeedBack::getRating).sum() / feedbacks.size();
    }

    // ==================== METODI PRIVATI ====================

    private void associaRelazioni(Event event, EventDTO dto) {
        if (dto.getVenueId() != null) {
            event.setVenue(venueRepo.findById(dto.getVenueId()).orElse(null));
        }
        if (dto.getTagIds() != null) {
            event.setTags(tagRepo.findAllById(dto.getTagIds()));
        }
        if (dto.getSpeakerIds() != null) {
            event.setSpeakers(speakerRepo.findAllById(dto.getSpeakerIds()));
        }
    }

    private EventDTO toEventDTOWithDetails(Event event) {
        EventDTO dto = mapper.toDTO(event);
        populateAvailableSeats(event, dto);
        populateSpeakersAndTags(event, dto);
        return dto;
    }

    private List<EventDTO> toDTOsWithDetails(List<Event> events) {
        List<EventDTO> dtos = mapper.toDTOList(events);
        for (int i = 0; i < events.size(); i++) {
            populateAvailableSeats(events.get(i), dtos.get(i));
            populateSpeakersAndTags(events.get(i), dtos.get(i));
        }
        return dtos;
    }

    private void populateAvailableSeats(Event event, EventDTO dto) {
        int activeTickets = event.getTickets() == null ? 0 :
            (int) event.getTickets().stream()
                .filter(t -> "ACTIVE".equals(t.getStatus()))
                .count();
        dto.setAvailableSeats(event.getMaxSeats() - activeTickets);
    }

    private void populateSpeakersAndTags(Event event, EventDTO dto) {
        if (event.getSpeakers() != null) {
            dto.setSpeakerNames(
                event.getSpeakers().stream()
                    .map(s -> s.getFirstName() + " " + s.getLastName())
                    .collect(Collectors.toList())
            );
        }
        if (event.getTags() != null) {
            dto.setTagNames(
                event.getTags().stream()
                    .map(t -> t.getName())
                    .collect(Collectors.toList())
            );
        }
    }
}