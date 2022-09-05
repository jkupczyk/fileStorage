package com.example.luxdone.exception;

public class FileFormatException extends RuntimeException {
    public FileFormatException(final String message, final Object... args) {
        super(String.format(message, args));
    }

    public FileFormatException(final String message) {
        super(message);
    }
}
