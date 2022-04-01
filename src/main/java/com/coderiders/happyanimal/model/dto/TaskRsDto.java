package com.coderiders.happyanimal.model.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskRsDto {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private LocalDate date;
    @NotNull
    private LocalTime time;
    @NotNull
    private boolean completed;
    @NotNull
    private String repeatType;
}