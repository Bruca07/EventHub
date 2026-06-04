package com.academy.eventhub.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.academy.eventhub.entity.User;

public interface UserRepository extends JpaRepository<User,Integer> {
User findByUsername(String username);
}
