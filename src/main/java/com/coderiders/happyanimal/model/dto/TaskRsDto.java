package com.coderiders.happyanimal.model.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
    private String expiresDateTime;
    @NotNull
    private boolean completed;
    @NotNull
    private String repeatType;
    private String note;
    private Long animalId;
    private String animalName;
    private String animalKind;
}