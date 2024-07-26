package com.example.justlife.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message)
    {
        super(message);
    }
}
