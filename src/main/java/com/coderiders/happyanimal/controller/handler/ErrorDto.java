package com.coderiders.happyanimal.controller.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public
class ErrorDto {
    private int code;
    private HttpStatus status;
    private String description;
}
