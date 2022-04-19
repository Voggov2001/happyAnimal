package com.coderiders.happyanimal.model.dto;

import com.coderiders.happyanimal.model.Animal;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionRqDto {
    @NotNull
    private LocalDate date;
    @NotNull
    private List<Long> animalIdList;
}
