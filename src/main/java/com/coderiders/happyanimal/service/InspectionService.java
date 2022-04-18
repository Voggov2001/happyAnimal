package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.exceptions.BadRequestException;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.mapper.InspectionMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.InspectionRepository;
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
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionMapper inspectionMapper;
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    private final String ERROR_NOT_FOUND_INSPECTION = "Осмотр не найден";
    private final String ERROR_NOT_FOUND_ANIMAL = "Животное не найдено";

    @Autowired
    public InspectionService(InspectionRepository inspectionRepository,
                             InspectionMapper inspectionMapper,
                             AnimalRepository animalRepository,
                             AnimalMapper animalMapper) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionMapper = inspectionMapper;
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }


    @Transactional
    public InspectionRsDto getById(Long id) {
        Inspection inspection = this.inspectionRepository.getById(id);
        return this.inspectionMapper.mapToRsDto(inspection);
    }

    @Transactional
    public Page<InspectionRsDto> getAll(Pageable pageable) {
        Page<Inspection> allInspection = inspectionRepository.findAll(pageable);
        if (allInspection.isEmpty()) {
            throw new NotFoundException("Запланированных осмотров нет!");
        } else {
            return allInspection.map(inspectionMapper::mapToRsDto);
        }
    }

    @Transactional
    public InspectionRsDto addAnimalToInspection(String date, Long animalId) {
        LocalDate localDate = Optional.ofNullable(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .orElseThrow(() -> new BadRequestException("Дата некорректна"));
        Optional<Inspection> inspectionOpt = inspectionRepository.findByDate(localDate);
        Inspection inspection;
        if (inspectionOpt.isPresent()) {
            inspection = inspectionOpt.get();
            List<Animal> animalList = inspection.getAnimalList();
            Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL));
            if (!animalList.contains(animal)) {
                animalList.add(animal);
                inspection.setAnimalList(animalList);
                inspectionRepository.save(inspection);
                animal.setStatus(AnimalStatus.BOOKED);
                animalRepository.save(animal);
            }
        } else {
            Animal animal = animalRepository.findById(animalId).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL));
            inspection = inspectionRepository.save(Inspection.builder()
                    .date(localDate)
                    .animalList(List.of(animal))
                    .build()
            );
            animal.setStatus(AnimalStatus.BOOKED);
            animalRepository.save(animal);

        }
        return inspectionMapper.mapToRsDto(inspection);
    }

    @Transactional
    public InspectionRsDto update(InspectionRqDto inspectionRqDto) {
        if (inspectionRepository.findByDate(inspectionRqDto.getDate()).isPresent()) {
            Inspection inspection = inspectionRepository.findByDate(inspectionRqDto.getDate()).get();
            inspection.setAnimalList(inspectionRqDto.getAnimalIdList()
                    .stream()
                    .map(aLong -> animalRepository.findById(aLong)
                            .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL)))
                    .collect(Collectors.toList()));
        }
        Inspection inspection = inspectionMapper.mapToInspection(inspectionRqDto);
        return inspectionMapper.mapToRsDto(inspectionRepository.save(inspection));
    }

    @Transactional
    public InspectionRsDto getByDate(String date) {
        LocalDate localDate = Optional.ofNullable(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .orElseThrow(() -> new BadRequestException("Дата некорректна"));
        return inspectionMapper
                .mapToRsDto(inspectionRepository.findByDate(localDate)
                        .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_INSPECTION)));
    }

    @Transactional
    public List<AnimalRsDto> getAnimals(Long id) {
        return inspectionRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_INSPECTION))
                .getAnimalList()
                .stream()
                .map(animalMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InspectionRsDto deleteAnimal(String date, Long animalId) {
        LocalDate localDate = Optional.ofNullable(LocalDate.parse(date, DateTimeFormatter.ISO_DATE))
                .orElseThrow(() -> new BadRequestException("Дата некорректна"));
        Inspection inspection = inspectionRepository.findByDate(localDate)
                .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_INSPECTION));
        List<Animal> animalList = inspection.getAnimalList();
        animalList.remove(animalRepository.findById(animalId).orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_ANIMAL)));
        inspection.setAnimalList(animalList);
        return inspectionMapper.mapToRsDto(inspectionRepository.save(inspection));
    }
}
