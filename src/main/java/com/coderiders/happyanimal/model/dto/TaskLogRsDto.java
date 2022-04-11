package com.coderiders.happyanimal.model.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLogRsDto {
    private Long id;
    private Long taskId;
    private Long userId;
    private String taskType;
    private String expiresDateTime;
    private String completedDateTime;
    private String repeatType;
    private Long animalId;
    private String animalName;
    private String animalKind;
    private String note;
}
