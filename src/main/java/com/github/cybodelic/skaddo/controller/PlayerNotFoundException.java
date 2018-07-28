package com.github.cybodelic.skaddo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String userId) {
        super("could not find player '" + userId + "'.");
    }
}
