package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.academy.eventhub.dto.ProfileDTO;
import com.academy.eventhub.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
// Converte da Entità a DTO
    ProfileDTO toDTO(Profile profile);


    // Converte da DTO a Entità
    Profile toEntity(ProfileDTO profileDTO);

    // Converte una lista di Entità in una lista di DTO
    List<ProfileDTO> toDTOList(List<Profile> profiles);

    // Converte una lista di DTO in una lista di Entità
    List<Profile> toEntityList(List<ProfileDTO> profileDTOs);
}

