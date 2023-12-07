package com.NewFeed.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AmazonS3WithBucketException extends RuntimeException {
    public AmazonS3WithBucketException(String message) {
        super(message);
    }
}
