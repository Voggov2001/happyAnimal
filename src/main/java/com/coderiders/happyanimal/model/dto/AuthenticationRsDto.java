package com.coderiders.happyanimal.model.dto;

import com.coderiders.happyanimal.enums.UserRole;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRsDto {
    private Long id;
    private UserRole userRole;
    private String token;
}
