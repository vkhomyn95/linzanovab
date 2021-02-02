package com.backend.linzanova.exeption;

public class DoesNotExistException extends RuntimeException {

    public DoesNotExistException(String message) {
        super(message);
    }
}
