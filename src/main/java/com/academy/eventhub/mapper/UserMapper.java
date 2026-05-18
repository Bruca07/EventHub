package com.academy.eventhub.mapper;
import java.util.List;

import org.mapstruct.Mapper;

import com.academy.eventhub.dto.UserRequestDTO;
import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.entity.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

// Da Entity a ResponseDTO (output)
    UserResponseDTO toResponseDTO(User user);
    List<UserResponseDTO> toResponseDTOList(List<User> users);

    // Da RequestDTO a Entity (input)
    User toEntity(UserRequestDTO dto);
}
