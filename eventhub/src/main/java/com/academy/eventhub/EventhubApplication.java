package com.academy.eventhub;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EventhubApplication {
public static void main(String[] args) {
        SpringApplication.run(EventhubApplication.class, args);
    }

    @Bean
CommandLineRunner debugPassword() {
    return args -> {
        System.out.println(new BCryptPasswordEncoder()
            .encode("demo 123"));
    };
}
    
}
