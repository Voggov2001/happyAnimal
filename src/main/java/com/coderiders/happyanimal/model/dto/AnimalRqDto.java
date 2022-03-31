package com.coderiders.happyanimal.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalRqDto {
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
    private String kind;
    private Long userId;
    private String featuresOfKeeping;
    private String externalFeatures;
    private LocalDate localDate;
}
