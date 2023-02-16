package br.com.newgo.spring.marketng.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(value = {NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFound(){
        return ResponseEntity.notFound().build();
    }
    @ExceptionHandler(value = {DataIntegrityViolationException.class,
                                IllegalArgumentException.class,
                                IllegalStateException.class,
                                EntityExistsException.class})
        public ResponseEntity<Object> handleResourceAlreadyExists(){
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleArgumentNotValid(MethodArgumentNotValidException ex){
        return ResponseEntity.badRequest().body(
                ex.getFieldErrors()
                        .stream()
                        .map(ErrorValidationDto::new)
                        .toList());
    }

}
