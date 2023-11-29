package com.NewFeed.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserProfileException extends RuntimeException {
    public UserProfileException(String message) {
        super(message);
    }
}