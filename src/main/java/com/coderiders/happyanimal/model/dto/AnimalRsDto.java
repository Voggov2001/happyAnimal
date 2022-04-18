package com.coderiders.happyanimal.model.dto;

import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRsDto {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String gender;
    @NotNull
    private int age;
    @NotNull
    private int height;
    @NotNull
    private double weight;
    @NotNull
    private AnimalKindDto animalKindDto;
    @NotNull
    private String status;
    private String location;
    private String animalStatus;
    private String featuresOfKeeping;
    private String externalFeatures;
    private UserRsDto userRsDto;
}
