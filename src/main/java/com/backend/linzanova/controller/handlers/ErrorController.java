package com.backend.linzanova.controller.handlers;

import com.backend.linzanova.exeption.DoesNotExistException;
import com.backend.linzanova.exeption.NoRightsException;
import com.backend.linzanova.exeption.TokenExpiredExeption;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), "Invalid product id", ex.getMessage());
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
//        response.sendError(403, "Your Message");
    }
}
