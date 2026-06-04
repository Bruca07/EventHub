package com.academy.eventhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.academy.eventhub.repository.EventRepository;

import jakarta.transaction.Transactional;

@SpringBootTest 
@Transactional 
public class EventServiceTest {
@Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepo;

    @Test
    void testFindByIdRealDb() {
       
        var result = eventService.findById(1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
    }
}

