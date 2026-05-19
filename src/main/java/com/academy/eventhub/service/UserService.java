package com.academy.eventhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.UserRequestDTO;
import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.entity.User;
import com.academy.eventhub.mapper.UserMapper;
import com.academy.eventhub.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserMapper mapper;

    @Autowired
    PasswordEncoder pw;

   public List<UserResponseDTO> findAll(){
    return mapper.toResponseDTOList(userRepo.findAll());
   } 

   public UserResponseDTO register(UserRequestDTO dto){
    User user = mapper.toEntity(dto);
    user.setPassword(pw.encode(dto.getPassword()));
    return mapper.toResponseDTO(userRepo.save(user));
   }
    

}
