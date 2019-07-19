package com.github.cybodelic.skaddo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(String.format("Invalid data: %s.", message));
    }
}
