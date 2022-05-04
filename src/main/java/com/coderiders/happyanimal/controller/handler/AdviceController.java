package com.coderiders.happyanimal.controller.handler;

import com.coderiders.happyanimal.exceptions.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceController {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class ErrorDto {
        private String description;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity handleValidationExceptions(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {InternalServerException.class})
    public ResponseEntity handleInternalServerException(InternalServerException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {UnAuthorizedException.class})
    public ResponseEntity handleUnAuthorizedException(UnAuthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDto(ex.getLocalizedMessage()));
    }
}
