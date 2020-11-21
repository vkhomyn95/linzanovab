package com.backend.linzanova.controller.goods.lens;

import com.backend.linzanova.exeption.TokenExpiredExeption;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

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


}
