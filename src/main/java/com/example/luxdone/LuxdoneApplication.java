package com.example.luxdone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LuxdoneApplication {

    public static void main(final String[] args) {
        SpringApplication.run(LuxdoneApplication.class, args);
    }

}
