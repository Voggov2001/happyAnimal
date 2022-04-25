package com.coderiders.happyanimal.security;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {
    private String login;
    private String password;
}
