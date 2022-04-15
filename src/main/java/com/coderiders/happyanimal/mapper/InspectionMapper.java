package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InspectionMapper {

    public Inspection mapToInspection(InspectionRqDto dto) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(dto, Inspection.class);
    }

    public InspectionRsDto mapToRsDto(Inspection inspection) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(inspection, InspectionRsDto.class);
    }
}
