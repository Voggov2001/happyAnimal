package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.mapper.ExhibitionMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Exhibition;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
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

@Service
public class ExhibitionService {
    private final ExhibitionRepository exhibitionRepository;
    private final ExhibitionMapper exhibitionMapper;
    private final AnimalRepository animalRepository;
    private static final String ERROR_MESSAGE_NOT_FOUND = "Выставка не найдена";

    @Autowired
    public ExhibitionService(ExhibitionRepository exhibitionRepository, ExhibitionMapper exhibitionMapper, AnimalRepository animalRepository) {
        this.exhibitionRepository = exhibitionRepository;
        this.exhibitionMapper = exhibitionMapper;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public ExhibitionRsDto saveExhibition(ExhibitionRqDto exhibitionRqDto) {
        Exhibition exhibition = exhibitionMapper.mapToExhibition(exhibitionRqDto);
        return exhibitionMapper.mapToDto(exhibitionRepository.save(exhibition));
    }

    @Transactional
    public Page<ExhibitionRsDto> getAll(Pageable pageable) {
        Page<Exhibition> allAnimals = exhibitionRepository.findAll(pageable);
        return allAnimals.map(exhibitionMapper::mapToDto);
    }

    @Transactional
    public ExhibitionRsDto findByDate(String date) {
        Exhibition exhibition = exhibitionRepository.findByDate(date).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND));
        return exhibitionMapper.mapToDto(exhibition);
    }

    @Transactional
    public void deleteExhibitionById(Long id) {
        exhibitionRepository.deleteExhibitionById(id);
    }

    @Transactional
    public void addAnimalIntoExhibition(Long id) {
        Animal animal = animalRepository.getById(id);
        ExhibitionRsDto exhibition = exhibitionMapper.mapToDto(exhibitionRepository.getById(id));
        exhibition.getAnimals().add(animal);
    }


}
