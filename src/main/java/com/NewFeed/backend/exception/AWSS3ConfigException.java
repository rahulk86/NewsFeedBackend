package com.NewFeed.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AWSS3ConfigException extends RuntimeException {
    public AWSS3ConfigException(String message) {
        super(message);
    }
}
