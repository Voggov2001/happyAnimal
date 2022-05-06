package com.coderiders.happyanimal.services;


import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.AnimalKind;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.service.AnimalService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AnimalServiceTest {

    @Autowired
    private AnimalService animalService;

    @MockBean
    private AnimalMapper animalMapper;

    @MockBean
    private AnimalRepository animalRepository;

    @MockBean
    private UserRepository userRepository;

    AnimalKind animalKind1 = new AnimalKind("Жираф", "Млекопитающие", "dsfsdfsmfk", "Парнокопытные".getBytes(StandardCharsets.UTF_8));

    User user = User.builder()
            .age(47)
            .name("Иван")
            .build();

    Animal animal = Animal.builder()
            .id(1L)
            .name("Шарик")
            .gender("male")
            .age(15)
            .height(155)
            .weight(228)
            .animalKind(animalKind1)
            .featuresOfKeeping("Пятно на лбу")
            .externalFeatures("закрытый вальер")
            .status(AnimalStatus.FINE)
            .location("Вальер1")
            .user(user)
            .build();

    AnimalRqDto rqDto = AnimalRqDto.builder()
            .kind(animal.getAnimalKind().getKind())
            .name(animal.getName())
            .gender(animal.getGender())
            .age(animal.getAge())
            .height(animal.getHeight())
            .weight(animal.getWeight())
            .featuresOfKeeping(animal.getFeaturesOfKeeping())
            .externalFeatures(animal.getExternalFeatures())
            .location(animal.getLocation())
            .userId(user.getId())
            .status(animal.getStatus().getName())
            .build();

    @Test
    void save() {

        animalService.saveAnimal(rqDto);
        verify(animalRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        verify(animalMapper, Mockito.times(1)).mapToAnimal(rqDto);
        verify(animalMapper, Mockito.times(1)).mapToDto(ArgumentMatchers.any());
    }

    @Test
    void getAllByUserId() {
        assertThrows(Exception.class, () -> animalService.getAllByUserId(Pageable.unpaged(), 1L));
    }

    @Test
    void getById() {
        Optional<Animal> animal1 = Optional.of(animal);
        doReturn(animal1).when(animalRepository).findById(ArgumentMatchers.any());
        animalService.getById(1L);
        verify(animalRepository, Mockito.times(1)).findById(1L);

    }

    @Test
    void editAnimal() {
        List<Animal> animal1 = new ArrayList<>();
        animal1.add(animal);
        doReturn(animal1).when(userRepository).getById(ArgumentMatchers.any());
        doReturn(animal).when(animalRepository).getById(ArgumentMatchers.any());
        doReturn(animal).when(animalMapper).mapToAnimal(ArgumentMatchers.any());
        animalService.editAnimal(1L, rqDto);
        verify(animalRepository).getById(1L);
        verify(animalRepository).save(ArgumentMatchers.any());
        verify(animalMapper).mapToAnimal(ArgumentMatchers.any());
        verify(animalMapper).mapToDto(ArgumentMatchers.any());
    }

    @Test
    void getPermittedAnimals() {
        List<Animal> animal1 = new ArrayList<>();
        animal1.add(animal);
        doReturn(animal1).when(animalRepository).findAll();
        animalService.getPermittedAnimals();
        verify(animalRepository, times(1)).findAll();
        verify(animalMapper, times(1)).mapToDto(ArgumentMatchers.any());
    }
}
