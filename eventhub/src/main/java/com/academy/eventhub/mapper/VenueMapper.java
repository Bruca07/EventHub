package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.academy.eventhub.dto.VenueDTO;
import com.academy.eventhub.entity.Venue;

@Mapper(componentModel = "spring")
public interface VenueMapper {

    // Da Entity a VenueDTO
    @Mapping(source = "id", target = "id")
    VenueDTO toDTO(Venue venue);
    List<VenueDTO>toDTOList(List<Venue> venues);


    //Da VenueDTO  a Entity
    Venue toEntity(VenueDTO dto);
    List<Venue>toEntityList(List<VenueDTO>venueDTOs);

}
