package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InspectionMapper {

    public InspectionRqDto mapToRqDto(Inspection inspection) {
        ModelMapper modelMapper = new ModelMapper();
        return (InspectionRqDto)modelMapper.map(inspection, InspectionRqDto.class);
    }

    @Transactional
    public Inspection mapToInspection(InspectionRqDto dto) {
        ModelMapper mapper = new ModelMapper();
        return (Inspection)mapper.map(dto, Inspection.class);
    }

    @Transactional
    public InspectionRsDto mapToRsDto(Inspection inspection) {
        ModelMapper mapper = new ModelMapper();
        return (InspectionRsDto)mapper.map(inspection, InspectionRsDto.class);
    }
}
