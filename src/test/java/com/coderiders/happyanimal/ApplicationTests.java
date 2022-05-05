package com.coderiders.happyanimal;

import com.coderiders.happyanimal.controller.AnimalKindController;
import com.coderiders.happyanimal.mapper.AnimalKindMapper;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import com.coderiders.happyanimal.service.AnimalKindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ApplicationTests {
    private AnimalKindRepository animalKindRepository;
    private AnimalKindService animalKindService;
    private AnimalKindController animalKindController;
    private AnimalKindMapper animalKindMapper;

    @Autowired
    public ApplicationTests(AnimalKindRepository animalKindRepository, AnimalKindService animalKindService, AnimalKindController animalKindController, AnimalKindMapper animalKindMapper) {
        this.animalKindRepository = animalKindRepository;
        this.animalKindService = animalKindService;
        this.animalKindController = animalKindController;
        this.animalKindMapper = animalKindMapper;
    }

}
