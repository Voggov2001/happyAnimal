package com.coderiders.happyanimal.services;

import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.mapper.InspectionMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.InspectionRepository;
import com.coderiders.happyanimal.service.InspectionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class InspectionServiceTest {

    @Autowired
    private InspectionService inspectionService;

    @MockBean
    private InspectionRepository inspectionRepository;

    @MockBean
    private InspectionMapper inspectionMapper;

    @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private AnimalMapper animalMapper;

    @Test
    void update(){
        InspectionRqDto inspectionRqDto = InspectionRqDto.builder().date(LocalDate.now()).build();
        inspectionService.update(inspectionRqDto);
        verify(inspectionRepository).save(ArgumentMatchers.any());
        verify(inspectionMapper).mapToInspection(ArgumentMatchers.any());
    }

    @Test
    void findByDate(){
        Optional<Inspection> optionalInspection = Optional.of(Inspection.builder().date(LocalDate.now()).build());
        doReturn(optionalInspection).when(inspectionRepository).findByDate(ArgumentMatchers.any());
        inspectionService.getByDate(LocalDate.now().toString());
    }

    @Test
    void addAnimal(){
        Animal animal = Animal.builder().id(1L).build();
        Optional<Animal> optionalAnimal= Optional.of(animal);
        String date = LocalDate.now().toString();
        doReturn(optionalAnimal).when(animalRepository).findById(ArgumentMatchers.any());
        inspectionService.addAnimalToInspection(LocalDate.now().toString(),animal.getId());
    }

    @Test
    void getAnimals(){
        List<Animal> animalList =  new ArrayList();
        animalList.add(Animal.builder().id(1L).name("Жучка").build());
        doReturn(AnimalRsDto.builder().id(1L).name("Жучка").build()).when(animalMapper).mapToDto(ArgumentMatchers.any());

        Optional<Inspection> optionalInspection = Optional.of(Inspection.builder().date(LocalDate.now()).animalList(animalList).build());
        doReturn(optionalInspection).when(inspectionRepository).findById(1L);
        inspectionService.getAnimals(1L);

    }

}
