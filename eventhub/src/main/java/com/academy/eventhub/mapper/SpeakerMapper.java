package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.academy.eventhub.dto.SpeakerDTO;
import com.academy.eventhub.entity.Speaker;

@Mapper(componentModel = "spring")
public interface SpeakerMapper {

    // Entity a SpeakerDTO
    SpeakerDTO toDTO(Speaker speaker);
    List<SpeakerDTO> toDTOList(List<Speaker> speakers);

    //Da SpeakerDTO a Enity
    Speaker toEntity(SpeakerDTO dto);

    List<Speaker> toEntityList(List<SpeakerDTO>speakerDTOs);

}
