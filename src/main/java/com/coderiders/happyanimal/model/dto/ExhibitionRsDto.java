package com.coderiders.happyanimal.model.dto;

import com.coderiders.happyanimal.model.Animal;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionRsDto {
    @NotNull
    @Min(1)
    private Long id;
    @NotNull
    private String date;
    @NotNull
    private List<AnimalRsDto> animals;
}
