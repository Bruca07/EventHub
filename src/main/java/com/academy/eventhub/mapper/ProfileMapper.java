package com.academy.eventhub.mapper;

import org.mapstruct.Mapper;

import com.academy.eventhub.dto.ProfileDTO;
import com.academy.eventhub.entity.Profile;
@Mapper(componentModel = "spring")
public interface ProfileMapper {
// Converte da Entità a DTO
    ProfileDTO toDTO(Profile profile);

    // Converte da DTO a Entità
    Profile toEntity(ProfileDTO profileDTO);
}
