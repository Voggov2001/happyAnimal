package com.coderiders.happyanimal.controller.handler;

import com.coderiders.happyanimal.exceptions.*;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ErrorDto> handleBadRequestException(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorDto(HttpStatus.NOT_FOUND.value(),
                        HttpStatus.NOT_FOUND,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ErrorDto> handleValidationExceptions(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {InternalServerException.class})
    public ResponseEntity<ErrorDto> handleInternalServerException(InternalServerException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    public ResponseEntity<ErrorDto> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDto(HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {UnAuthorizedException.class})
    public ResponseEntity<ErrorDto> handleUnAuthorizedException(UnAuthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorDto> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDto(HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    protected ResponseEntity<ErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorDto(HttpStatus.FORBIDDEN.value(),
                        HttpStatus.FORBIDDEN,
                        ex.getLocalizedMessage()));
    }

    @ExceptionHandler(value = {JwtException.class})
    public ResponseEntity<ErrorDto> handleMalformedJwtExceptionException(JwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto(HttpStatus.UNAUTHORIZED.value(),
                        HttpStatus.UNAUTHORIZED,
                        ex.getLocalizedMessage()));
    }
}
