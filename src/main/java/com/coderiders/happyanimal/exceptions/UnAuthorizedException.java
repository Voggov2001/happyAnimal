package com.coderiders.happyanimal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends AuthenticationException {
    public UnAuthorizedException(String message) {
        super(message);
    }
}