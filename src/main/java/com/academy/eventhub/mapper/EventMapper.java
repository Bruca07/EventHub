package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.academy.eventhub.dto.EventDTO;
import com.academy.eventhub.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    // Da Entity a EventDTO
    EventDTO toDTO(Event event);
    List<EventDTO>toDTOList(List<Event> events);

    // Da EventDTO a Entity
    Event toEntity(EventDTO dto);
    List<Event>toEntityList(List<EventDTO>eventDTOs);



}
