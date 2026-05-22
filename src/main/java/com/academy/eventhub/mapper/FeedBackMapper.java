package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.academy.eventhub.dto.FeedBackDTO;
import com.academy.eventhub.entity.FeedBack;

@Mapper(componentModel = "spring")
public interface FeedBackMapper {

    // Entity a DTO
    FeedBackDTO toDTO(FeedBack feedBack);
    List<FeedBackDTO> toDTOList(List<FeedBack> feedbacks);

    
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "event", ignore = true)
    FeedBack toEntity(FeedBackDTO dto);
    
    List<FeedBack> toEntityList(List<FeedBackDTO> feedbackDTOs);
}
