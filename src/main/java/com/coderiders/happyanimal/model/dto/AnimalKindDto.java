package com.coderiders.happyanimal.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalKindDto {
    private String animalClass;
    private String squad;
    private String kind;
    private String pic;

    public AnimalKindDto(String animalClass, String squad, String kind) {
        this.animalClass = animalClass;
        this.squad = squad;
        this.kind = kind;
    }
}
