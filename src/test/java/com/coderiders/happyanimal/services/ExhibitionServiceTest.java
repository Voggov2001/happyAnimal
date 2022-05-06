package com.coderiders.happyanimal.services;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.mapper.ExhibitionMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Exhibition;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRqDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.ExhibitionRepository;
import com.coderiders.happyanimal.service.ExhibitionService;
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
import static org.mockito.Mockito.*;

@SpringBootTest
public class ExhibitionServiceTest {

    @Autowired
    private ExhibitionService exhibitionService;

    @MockBean
    private ExhibitionRepository exhibitionRepository;

    @MockBean
    private ExhibitionMapper exhibitionMapper;

    @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private AnimalMapper animalMapper;

    @Test
    void update() {
        ExhibitionRqDto exhibitionRqDto = ExhibitionRqDto.builder().date(LocalDate.now().toString()).build();
        exhibitionService.update(exhibitionRqDto);
        verify(exhibitionRepository).findByDate(ArgumentMatchers.any());
        verify(exhibitionMapper).mapToRsDto(ArgumentMatchers.any());
        verify(exhibitionRepository).save(ArgumentMatchers.any());
    }

    @Test
    void findByDate() {
        assertThrows(NotFoundException.class, () -> exhibitionService.findByDate(LocalDate.now().toString()));
    }

    @Test
    void addAnimal(){
        Animal animal = Animal.builder().id(1L).build();
        Optional<Animal> optionalAnimal= Optional.of(animal);
        String date = LocalDate.now().toString();
        doReturn(optionalAnimal).when(animalRepository).findById(ArgumentMatchers.any());
        exhibitionService.addAnimalToExhibition(date,animal.getId());
    }

    @Test
    void getById(){
        Optional<Exhibition> optionalExhibition = Optional.of(Exhibition.builder().id(1L).build());
        doReturn(optionalExhibition).when(exhibitionRepository).findById(1L);
        exhibitionService.getById(1L);
    }

    @Test
    void getAllAnimals(){
        List<Animal> animalList =  new ArrayList();
        animalList.add(Animal.builder().id(1L).name("Жучка").build());
        doReturn(AnimalRsDto.builder().id(1L).name("Жучка").build()).when(animalMapper).mapToDto(ArgumentMatchers.any());

        Optional<Exhibition> optionalExhibition = Optional.of(Exhibition.builder().id(1L).animals(animalList).build());
        doReturn(optionalExhibition).when(exhibitionRepository).findById(1L);
        exhibitionService.getAllAnimals(1L);
    }

    @Test
    void saveExhibition(){
        ExhibitionRqDto exhibitionRqDto = ExhibitionRqDto.builder().date(LocalDate.now().toString()).build();
        exhibitionService.saveExhibition(exhibitionRqDto);
        verify(exhibitionRepository).save(ArgumentMatchers.any());
    }
}
