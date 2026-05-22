package com.academy.eventhub.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.academy.eventhub.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
}
