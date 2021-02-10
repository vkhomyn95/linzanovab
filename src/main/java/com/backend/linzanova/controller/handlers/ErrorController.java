package com.backend.linzanova.controller.handlers;

import com.backend.linzanova.exeption.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.FileAlreadyExistsException;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = "Object " +
                fieldError.getObjectName() +
                ", field " +
                fieldError.getField() +
                " - " +
                fieldError.getDefaultMessage();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Заповніть усі поля", message);
    }

    @ExceptionHandler(TokenExpiredExeption.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenExpiredExeption(TokenExpiredExeption ex) {
        return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Token expired", ex.getMessage());
    }

    @ExceptionHandler(DoesNotExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDoesNotExistException(RuntimeException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Invalid id", ex.getMessage());
    }

    @ExceptionHandler(NoRightsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNoRightsException(RuntimeException ex) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), "No rights for user", ex.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNoRightsExceptionInRoute(Exception response) {
        return new ErrorResponse(HttpStatus.FORBIDDEN.value(), "No rights for user", response.getMessage());
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileAlreadyExistsException(Exception response) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Already exists", response.getMessage());
    }

    @ExceptionHandler(value = NoSuchFileException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchFileException(Exception response) {
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Not Found", response.getMessage());
    }

}
