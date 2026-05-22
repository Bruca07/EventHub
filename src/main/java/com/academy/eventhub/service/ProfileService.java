package com.academy.eventhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.ProfileDTO;
import com.academy.eventhub.entity.Profile;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.ProfileMapper;
import com.academy.eventhub.repository.ProfileRepository;

@Service
public class ProfileService {


    @Autowired
    ProfileRepository profileRepo;

    @Autowired
    ProfileMapper mapper;

    public List<ProfileDTO> findAll(){
        return mapper.toDTOList(profileRepo.findAll());
    }

    public ProfileDTO findById(int id) {
    Profile profile = profileRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

    return mapper.toDTO(profile);
}

    public ProfileDTO save(ProfileDTO dto) {
    Profile profile = mapper.toEntity(dto);
    Profile saved = profileRepo.save(profile);
    return mapper.toDTO(saved);
}

public ProfileDTO update(int id, ProfileDTO dto){
    Profile profile = profileRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

    profile.setFirstName(dto.getFirstName());
    profile.setLastName(dto.getLastName());
    profile.setBio(dto.getBio());
    profile.setCity(dto.getCity());
    profile.setPhoto(dto.getPhoto());

    Profile updatedProfile = profileRepo.save(profile);
    return mapper.toDTO(updatedProfile);
}

    public void delete(int id){
    profileRepo.deleteById(id);
}

public ProfileDTO findByUserUsername(String username) {

    Profile profile = profileRepo.findByUserUsername(username);

    if (profile == null) {
        throw new RuntimeException("Profile not found for user: " + username);
    }

    return mapper.toDTO(profile);
}
}


