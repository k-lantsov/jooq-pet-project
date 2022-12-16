package com.company.jooqpetproject.exception;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }
}
