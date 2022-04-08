package com.coderiders.happyanimal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HappyAnimalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HappyAnimalApplication.class, args);
    }
}