package com.backend.linzanova.exeption;

public class NoRightsException extends RuntimeException {
    public NoRightsException(String message) {
        super(message);
    }
}
