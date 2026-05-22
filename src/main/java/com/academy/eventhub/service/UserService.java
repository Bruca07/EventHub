package com.academy.eventhub.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.academy.eventhub.dto.UserRequestDTO;
import com.academy.eventhub.dto.UserResponseDTO;
import com.academy.eventhub.entity.Role;
import com.academy.eventhub.entity.User;
import com.academy.eventhub.exception.ResourceNotFoundException;
import com.academy.eventhub.mapper.UserMapper;
import com.academy.eventhub.repository.RoleRepository;
import com.academy.eventhub.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    UserMapper mapper;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    PasswordEncoder pw;

   public List<UserResponseDTO> findAll(){
    return mapper.toResponseDTOList(userRepo.findAll());
   } 

   public UserResponseDTO register(UserRequestDTO dto){
    User user = mapper.toEntity(dto); 
    user.setPassword(pw.encode(dto.getPassword()));
    user.setEnabled(true);
    Role defaultRole = roleRepo.findByName("ROLE_USER");
    user.setRole(defaultRole);
    return mapper.toResponseDTO(userRepo.save(user));
}

// metodo per hashare la password
public void printAdminPasswordHash() {
    System.out.println(pw.encode("admin123"));
}

public UserResponseDTO findById(int id){
    User user = userRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return mapper.toResponseDTO(user);
}

public UserResponseDTO update(int id, UserRequestDTO dto) {
    User user = userRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    user.setUsername(dto.getUsername());

    if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
        user.setPassword(pw.encode(dto.getPassword()));
    }

    User updatedUser = userRepo.save(user);
    return mapper.toResponseDTO(updatedUser);
}

public void delete(int id){
    userRepo.deleteById(id);
}

public UserResponseDTO changeRole(int id, String name) {

    User user = userRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    Role role = roleRepo.findByName(name);

    if (role == null) {
        throw new ResourceNotFoundException("Role not found: " + name);
    }

    user.setRole(role);

    User saved = userRepo.save(user);
    return mapper.toResponseDTO(saved);
}


    

}
