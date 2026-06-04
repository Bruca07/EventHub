package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.academy.eventhub.dto.TicketDTO;
import com.academy.eventhub.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    
@Mapping(source = "event.id", target = "eventId")
@Mapping(source = "user.username", target = "username")
TicketDTO toDTO(Ticket ticket);
List<TicketDTO>toDTOList(List<Ticket> ticket);

Ticket toEntity(TicketDTO dto);
List<Ticket>toEntityList(List<TicketDTO> dtos);
}