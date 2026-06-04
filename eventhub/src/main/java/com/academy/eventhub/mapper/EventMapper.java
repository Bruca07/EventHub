package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.academy.eventhub.dto.EventDTO;
import com.academy.eventhub.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    // Da Entity a EventDTO
        @Mapping(source = "id", target = "id")
        @Mapping(source = "venue.id", target = "venueId")
        @Mapping(source = "venue.name", target = "venueName")
        @Mapping(source = "venue.city", target = "venueCity")
        @Mapping(source = "user.id", target = "userId")
        @Mapping(source = "standardPrice", target = "standardPrice")
        @Mapping(source = "vipPrice", target = "vipPrice")
    EventDTO toDTO(Event event);
    List<EventDTO>toDTOList(List<Event> events);

    // Da EventDTO a Entity

    @Mapping(target = "venue", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tickets", ignore = true)
    @Mapping(target = "feedBacks", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "speakers", ignore = true)
    Event toEntity(EventDTO dto);
    List<Event>toEntityList(List<EventDTO>eventDTOs);



}
