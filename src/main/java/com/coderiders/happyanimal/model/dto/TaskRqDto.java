package com.coderiders.happyanimal.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRqDto {
    @NotNull
    private String type;
    @NotNull
    private LocalDateTime localDateTime;
    @NotNull
    private String repeatType;
    @NotNull
    private Long animalId;
}
