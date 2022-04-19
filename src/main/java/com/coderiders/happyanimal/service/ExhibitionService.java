package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.exceptions.BadRequestException;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.mapper.ExhibitionMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Exhibition;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRqDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.ExhibitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionMapper exhibitionMapper;
    private final AnimalMapper animalMapper;
    private final AnimalRepository animalRepository;

    private final String ERROR_NOT_FOUND_EXHIBITION = "Выставка не найдена";
    private final String ERROR_NOT_FOUND_ANIMAL = "Животное не найдено";


    @Autowired
    public ExhibitionService(ExhibitionRepository exhibitionRepository, ExhibitionMapper exhibitionMapper, AnimalRepository animalRepository, AnimalMapper animalMapper) {
        this.exhibitionRepository = exhibitionRepository;
        this.exhibitionMapper = exhibitionMapper;
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    @Transactional
    public ExhibitionRsDto update(ExhibitionRqDto exhibitionRqDto) {
        if (exhibitionRepository.findByDate(exhibitionRqDto.getDate()).isPresent()) {
            Exhibition exhibition = exhibitionRepository.findByDate(exhibitionRqDto.getDate()).get();
            exhibition.setAnimals(exhibitionRqDto.getAnimalIds()
                    .stream()
                    .map(aLong -> animalRepository.findById(aLong)
                            .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL)))
                    .collect(Collectors.toList()));
        }
        Exhibition exhibition = exhibitionMapper.mapToExhibition(exhibitionRqDto);
        return exhibitionMapper.mapToRsDto(exhibitionRepository.save(exhibition));
    }

    @Transactional
    public Page<ExhibitionRsDto> getAll(Pageable pageable) {
        Page<Exhibition> allAnimals = exhibitionRepository.findAll(pageable);
        return allAnimals.map(exhibitionMapper::mapToRsDto);
    }

    @Transactional
    public ExhibitionRsDto findByDate(String date) {
        LocalDate localDate = Optional.ofNullable(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .orElseThrow(() -> new BadRequestException("Дата некорректна"));
        return exhibitionMapper
                .mapToRsDto(exhibitionRepository.findByDate(localDate)
                        .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_EXHIBITION)));
    }

    @Transactional
    public ExhibitionRsDto addAnimalToExhibition(String date, Long animalId) {
        LocalDate localDate = Optional.ofNullable(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .orElseThrow(() -> new BadRequestException("Дата некорректна"));
        Optional<Exhibition> exhibitionOptional = exhibitionRepository.findByDate(localDate);
        Exhibition exhibition;
        if (exhibitionOptional.isPresent()) {
            exhibition = exhibitionOptional.get();
            List<Animal> animalList = exhibition.getAnimals();
            Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL));
            if (!animalList.contains(animal)) {
                animalList.add(animal);
                exhibition.setAnimals(animalList);
                exhibitionRepository.save(exhibition);
                animal.setStatus(AnimalStatus.BOOKED_EXHIBITION);
                animalRepository.save(animal);
            }
        } else {
            Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL));
            exhibition = exhibitionRepository.save(Exhibition.builder()
                    .date(localDate)
                    .animals(List.of(animal))
                    .build()
            );
            animal.setStatus(AnimalStatus.BOOKED_EXHIBITION);
            animalRepository.save(animal);
        }
        return exhibitionMapper.mapToRsDto(exhibition);
    }

    @Transactional
    public ExhibitionRsDto getById(Long id) {
        Exhibition exhibition = exhibitionRepository.findById(id).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_EXHIBITION));
        return exhibitionMapper.mapToRsDto(exhibition);
    }

    @Transactional
    public List<AnimalRsDto> getAllAnimals(Long id) {
        return exhibitionRepository.findById(id)
                .orElseThrow((() -> new NotFoundException(ERROR_NOT_FOUND_EXHIBITION)))
                .getAnimals()
                .stream()
                .map(animalMapper::mapToDto)
                .collect(Collectors.toList());

    }
}
