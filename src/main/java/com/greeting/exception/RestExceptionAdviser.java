package com.greeting.exception;


import com.greeting.dto.ResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@RestControllerAdvice
public class RestExceptionAdviser {

    private static final Logger log = LoggerFactory.getLogger(RestExceptionAdviser.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(RuntimeException exception) {
        if (log.isInfoEnabled()) {
            log.error("Error while runtime exception {}", exception.toString());
        }
        var dto = ResponseDto.responseMessage("Unable to process the request at this time");
        return new ResponseEntity<>(dto, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseDto> handleConflictError(ConflictException exception) {
        if (log.isInfoEnabled()) {
            log.error("Error while runtime exception {}", exception.getMessage());
        }
        var dto = ResponseDto.responseMessage(exception.getMessage());
        return new ResponseEntity<>(dto, CONFLICT);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ResponseDto> handleDataNotFoundException(DataNotFoundException dataNotFoundException) {
        var dto = ResponseDto.responseMessage(dataNotFoundException.getMessage());
        return new ResponseEntity<>(dto, NOT_FOUND);
    }

    @ExceptionHandler(SomethingWentWrong.class)
    public ResponseEntity<ResponseDto> handleSomethingWentWring(SomethingWentWrong somethingWentWrong) {
        var dto = ResponseDto.responseMessage(somethingWentWrong.getMessage());
        return new ResponseEntity<>(dto, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        for (var fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.error("Error {}", errors);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        log.error("Error {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }


    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Map<String, String> handleRequestParameterException(ConstraintViolationException exception) {

        var errors = new HashMap<String, String>();
        exception.getConstraintViolations().forEach(constraintViolation -> {
            var messageTemplate = constraintViolation.getMessageTemplate();
            errors.put("error", messageTemplate);
        });
        return errors;
    }
}
