package com.NewFeed.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class VoteException extends RuntimeException {
    public VoteException(String message) {
        super(message);
    }
}
