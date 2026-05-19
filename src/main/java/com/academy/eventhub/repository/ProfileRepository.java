package com.academy.eventhub.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.academy.eventhub.entity.Profile;
public interface ProfileRepository extends JpaRepository<Profile,Integer> {

}
