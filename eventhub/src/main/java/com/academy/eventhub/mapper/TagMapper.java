package com.academy.eventhub.mapper;

import java.util.List;

import org.mapstruct.Mapper;


import com.academy.eventhub.dto.TagDTO;

import com.academy.eventhub.entity.Tag;

@Mapper(componentModel = "spring")
public interface TagMapper {

    // Entity a TagDTO
    TagDTO toDTO(Tag tag);
    List<TagDTO> toDTOList(List<Tag> tags);

    //Da TagDTO a Enity
    Tag toEntity(TagDTO dto);

    List<Tag> toEntityList(List<TagDTO>tagDTOs);
}
