package com.backend.linzanova.exeption;

public class TokenExpiredExeption extends RuntimeException {
    public  TokenExpiredExeption(String s) {
        super(s);
    }
}
