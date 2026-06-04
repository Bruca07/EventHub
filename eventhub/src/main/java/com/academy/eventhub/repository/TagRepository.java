package com.academy.eventhub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.academy.eventhub.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer> {

}
