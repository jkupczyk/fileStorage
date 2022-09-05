package com.example.luxdone.exception;

public class NotFoundException extends Exception {
    public NotFoundException(final String message, final Object... args) {
        super(String.format(message, args));
    }
}
