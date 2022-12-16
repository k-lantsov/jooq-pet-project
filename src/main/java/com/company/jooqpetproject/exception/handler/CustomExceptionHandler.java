package com.company.jooqpetproject.exception.handler;

import com.company.jooqpetproject.exception.DataQueryException;
import com.company.jooqpetproject.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataNotFoundException(DataIntegrityViolationException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body("Please check country information");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataQueryException.class)
    public ResponseEntity<String> handleDataQueryException(DataQueryException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
