package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.academy.eventhub.dto.ProfileDTO;
import com.academy.eventhub.entity.Profile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    // Da Entità a ProfileDTO
    ProfileDTO toDTO(Profile profile);
    List<ProfileDTO>toDTOList(List<Profile>profiles);

    //da ProfileDTO a Entità
    Profile toEntity(ProfileDTO dto);
    List<Profile> toEntities(List<ProfileDTO>dtos);

}
