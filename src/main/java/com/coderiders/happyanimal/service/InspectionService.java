package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.InspectionMapper;
import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.repository.InspectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
@Service
public class InspectionService {
    private final InspectionRepository inspectionRepository;
    private final InspectionMapper inspectionMapper;

    @Autowired
    public InspectionService(InspectionRepository inspectionRepository, InspectionMapper inspectionMapper) {
        this.inspectionRepository = inspectionRepository;
        this.inspectionMapper = inspectionMapper;
    }

    @Transactional
    public InspectionRsDto saveInspection(InspectionRqDto dto) {
        Inspection inspection = this.inspectionMapper.mapToInspection(dto);
        this.inspectionRepository.save(inspection);
        return this.inspectionMapper.mapToRsDto(inspection);
    }

    @Transactional
    public InspectionRsDto getById(Long id) {
        Inspection inspection = this.inspectionRepository.getById(id);
        return this.inspectionMapper.mapToRsDto(inspection);
    }

    @Transactional
    public Page<InspectionRsDto> getAll(Pageable pageable) {
        Page<Inspection> allInspection = this.inspectionRepository.findAll(pageable);
        if (allInspection.isEmpty()) {
            throw new NotFoundException("Запланированных осмотров нет!");
        } else {
            InspectionMapper var10001 = this.inspectionMapper;
            Objects.requireNonNull(var10001);
            return allInspection.map(var10001::mapToRsDto);
        }
    }
}
