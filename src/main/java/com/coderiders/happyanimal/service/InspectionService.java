package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.InspectionMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionMapper inspectionMapper;
    private final AnimalRepository animalRepository;

    @Autowired
    public InspectionService(InspectionRepository inspectionRepository,
                             InspectionMapper inspectionMapper,
                             AnimalRepository animalRepository) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionMapper = inspectionMapper;
        this.animalRepository = animalRepository;
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
            InspectionMapper var10001 = inspectionMapper;
            Objects.requireNonNull(var10001);
            return allInspection.map(var10001::mapToRsDto);
        }
    }

    @Transactional
    public void addAnimalToInspection(Long animalId, LocalDate localDate) {
        Optional<Inspection> inspectionOpt = inspectionRepository.findByDate(localDate);
        if (inspectionOpt.isPresent()) {
            Inspection inspection = inspectionOpt.get();
            List<Animal> animalList = inspection.getAnimalList();
            Animal animal = animalRepository.getById(animalId);
            if (!animalList.contains(animal)) {
                animalList.add(animal);
                inspection.setAnimalList(animalList);
                inspectionRepository.save(inspection);
                animal.setStatus(AnimalStatus.BOOKED);
                animalRepository.save(animal);
            }
        } else {
            inspectionRepository.save(Inspection.builder()
                    .date(localDate)
                    .animalList(List.of(animalRepository.getById(animalId)))
                    .build()
            );
        }
    }
}
