package br.com.newgo.spring.marketng.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;
@Getter
@Setter
public class ErrorValidationDto {

    private String field;
    private String message;

    public ErrorValidationDto(FieldError error){
        this.field = error.getField();
        this.message = error.getDefaultMessage();
    }
}