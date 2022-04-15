package com.coderiders.happyanimal.model.dto;

import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime expiresDateTime;
    @NotNull
    private String repeatType;
    @NotNull
    private boolean isCompleted;
    @NotNull
    private Long animalId;

    private String note;
}
