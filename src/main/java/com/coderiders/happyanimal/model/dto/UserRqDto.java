package com.coderiders.happyanimal.model.dto;

import com.coderiders.happyanimal.enums.UserRole;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRqDto {
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private String patronymic;
    @NotNull
    private int age;
    @NotNull
    private String login;
    @NotNull
    private String password;
}
