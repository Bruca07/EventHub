package com.academy.eventhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.SpeakerDTO;
import com.academy.eventhub.entity.Speaker;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.SpeakerMapper;
import com.academy.eventhub.repository.SpeakerRepository;

@Service
public class SpeakerService {

    @Autowired
    SpeakerRepository speakerRepo;

    @Autowired
    SpeakerMapper mapper;

    public List<SpeakerDTO> findAll(){
        return mapper.toDTOList(speakerRepo.findAll());
    }

    public SpeakerDTO findById(int id){
       Speaker speaker = speakerRepo.findById(id)
       .orElseThrow(()-> new ResourceNotFoundException("Speaker not found"));
       return mapper.toDTO(speaker);
    }

    public SpeakerDTO save(SpeakerDTO dto){
        Speaker speaker = mapper.toEntity(dto);
        Speaker saved = speakerRepo.save(speaker);
        return mapper.toDTO(saved);
    }

    public SpeakerDTO update(int id,SpeakerDTO dto){
        Speaker speaker = speakerRepo.findById(id)
       .orElseThrow(()-> new ResourceNotFoundException("Speaker not found"));

       speaker.setFirstName(dto.getFirstName());
       speaker.setLastName(dto.getLastName());
       speaker.setCompany(dto.getCompany());
       
       Speaker updated = speakerRepo.save(speaker);
       return mapper.toDTO(updated);

    }

    public void deleteById(int id){
        speakerRepo.deleteById(id);
    }

}
