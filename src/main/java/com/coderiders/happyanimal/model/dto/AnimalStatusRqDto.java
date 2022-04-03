package com.coderiders.happyanimal.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnimalStatusRqDto {
    @NotNull
    private String name;
    @NotNull
    private boolean permissionToParticipate;
}
