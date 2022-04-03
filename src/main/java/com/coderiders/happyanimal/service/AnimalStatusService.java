package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalStatusMapper;
import com.coderiders.happyanimal.model.AnimalStatus;
import com.coderiders.happyanimal.model.dto.AnimalStatusRqDto;
import com.coderiders.happyanimal.model.dto.AnimalStatusRsDto;
import com.coderiders.happyanimal.repository.AnimalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalStatusService {
    private final AnimalStatusRepository animalStatusRepository;
    private final AnimalStatusMapper animalStatusMapper;
    private static final String ERROR_MESSAGE_NOT_FOUND = "Статус не найден";

    @Autowired
    public AnimalStatusService(AnimalStatusRepository animalStatusRepository, AnimalStatusMapper animalStatusMapper) {
        this.animalStatusRepository = animalStatusRepository;
        this.animalStatusMapper = animalStatusMapper;
    }

    @Transactional
    public AnimalStatusRsDto saveAnimal(AnimalStatusRqDto animalStatusRqDto) {
        AnimalStatus animalStatus = animalStatusMapper.mapToAnimal(animalStatusRqDto);
        return animalStatusMapper.mapToDto(animalStatusRepository.save(animalStatus));
    }

    @Transactional
    public Page<AnimalStatusRsDto> getAll(Pageable pageable) {
        return animalStatusRepository.findAll(pageable).map(animalStatusMapper::mapToDto);
    }

    @Transactional
    public AnimalStatusRsDto getById(Long id) {
        AnimalStatus animalStatus = animalStatusRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND));
        return animalStatusMapper.mapToDto(animalStatus);
    }

    @Transactional
    public List<AnimalStatusRsDto> getByPermissionToParticipate(boolean permissionToParticipate) {
        List<AnimalStatus> listPermissionToParticipate = animalStatusRepository.findByPermissionToParticipate(permissionToParticipate);
        return listPermissionToParticipate.stream()
                .map(animalStatusMapper::mapToDto)
                .collect(Collectors.toList());
    }

}
