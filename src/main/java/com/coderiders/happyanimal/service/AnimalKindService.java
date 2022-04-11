package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.mapper.AnimalKindMapper;
import com.coderiders.happyanimal.model.dto.AnimalKindDto;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalKindService {
    private final AnimalKindRepository animalKindRepository;
    private final AnimalKindMapper animalKindMapper;

    @Autowired
    public AnimalKindService(AnimalKindRepository animalKindRepository, AnimalKindMapper animalKindMapper) throws IOException {
        this.animalKindRepository = animalKindRepository;
        this.animalKindMapper = animalKindMapper;
        createAll();
    }

    @Transactional
    public void createAll() throws IOException {
        createKind(new AnimalKindDto("Млекопитающие", "Хищные", "Бурый медведь"),
                "src/main/resources/assets/bear.svg");
        createKind(new AnimalKindDto("Травоядные млекопитающие", "Хоботные", "Слон саванный африканский"),
                "src/main/resources/assets/elephant.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Хищные", "Лисица обыкновенная"),
                "src/main/resources/assets/fox.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Парнокопытные", "Жираф"),
                "src/main/resources/assets/giraffe.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Непарнокопытные", "Лошадь"),
                "src/main/resources/assets/horse.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Двурезцовые сумчатые", "Коала"),
                "src/main/resources/assets/koala.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Приматы", "Белогрудый капуцин"),
                "src/main/resources/assets/monkey.svg");
        createKind(new AnimalKindDto("Птицы", "Совообразные", "Сова полярная"),
                "src/main/resources/assets/owl.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Хищные", "Большая панда"),
                "src/main/resources/assets/panda.svg");
        createKind(new AnimalKindDto("Птицы", "Пингвинообразные", "Пингвин иимператорский"),
                "src/main/resources/assets/peng.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Хищные", "Енот-полоскун"),
                "src/main/resources/assets/raccoon.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Парнокопытные", "Овца гималайская"),
                "src/main/resources/assets/sheep.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Хищные", "Тигр амурский"),
                "src/main/resources/assets/tiger.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Хищные", "Волк"),
                "src/main/resources/assets/wolf.svg");
        createKind(new AnimalKindDto("Млекопитающие", "Непарнокопытные", "Зебра"),
                "src/main/resources/assets/zebra.svg");
    }


    @Transactional
    public void createKind(AnimalKindDto dto, String filename) throws IOException {
        System.out.println();
        dto.setPic(Base64.getEncoder()
                .encode(Files
                        .readAllBytes(new File(filename)
                                .toPath())));
        animalKindRepository.save(animalKindMapper.mapToAnimalKind(dto));
    }

    @Transactional
    public List<AnimalKindDto> getAll() {
        return animalKindRepository.findAll()
                .stream()
                .map(animalKindMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> getAllOnlyKinds() {
        return getAll().stream().map(AnimalKindDto::getKind).collect(Collectors.toList());
    }

    @Transactional
    public AnimalKindDto getByKindName(String name) {
        return animalKindMapper.mapToDto(animalKindRepository.getById(name));
    }

    public void deleteAll() {
        animalKindRepository.deleteAll();
    }
}
