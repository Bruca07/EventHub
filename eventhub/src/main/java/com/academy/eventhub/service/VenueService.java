package com.academy.eventhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.VenueDTO;
import com.academy.eventhub.entity.Venue;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.VenueMapper;
import com.academy.eventhub.repository.EventRepository;
import com.academy.eventhub.repository.VenueRepository;

@Service
public class VenueService {

    @Autowired
    VenueRepository  venueRepo;

    @Autowired
    VenueMapper mapper;

    @Autowired
    EventRepository eventRepo;

    public List<VenueDTO> findAll(){
        return mapper.toDTOList(venueRepo.findAll());
    }

    public VenueDTO findById(int id){
        Venue venue = venueRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Venue not found"));

        return mapper.toDTO(venue);
    }

    public VenueDTO save(VenueDTO dto){
        Venue venue = mapper.toEntity(dto);
        Venue saved = venueRepo.save(venue);
        return mapper.toDTO(saved);

    }

    public VenueDTO update(int id, VenueDTO dto){
        Venue venue = venueRepo.findById(id)
        .orElseThrow(()-> new ResourceNotFoundException("Venue not found"));
        
        venue.setName(dto.getName());
        venue.setAddress(dto.getAddress());
        venue.setCity(dto.getCity());
        venue.setCapacity(dto.getCapacity());

        Venue updateVenue = venueRepo.save(venue);
        return mapper.toDTO(updateVenue);
    }

    public void delete(int id) {
    Venue venue = venueRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
    
    // Sgancia gli eventi dalla sede prima di eliminarla
    venue.getEvents().forEach(event -> {
        event.setVenue(null);
        eventRepo.save(event);
    });
    
    venueRepo.deleteById(id);
}


}
