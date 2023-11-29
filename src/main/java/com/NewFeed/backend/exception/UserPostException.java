package com.NewFeed.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserPostException extends RuntimeException {
    public UserPostException(String message) {
        super(message);
    }
}
