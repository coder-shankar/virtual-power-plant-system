package org.virtualpowerplant.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestException(BadRequestException ex) {
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(ex.getMessage());
        response.setErrors(ex.getErrors());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, ex.getStatus());
    }


    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Object> handleMethodParamValidation(HandlerMethodValidationException ex) {

        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Bad Request");
        List<Error.ErrorDetail> fieldErrors = ex.getAllErrors()
                                                .stream()
                                                .map(e -> new Error.ErrorDetail(((FieldError) e).getField(), ((FieldError) e).getDefaultMessage()))
                                                .toList();
        response.setErrors(fieldErrors);
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Internal server error", ex);
        ExceptionResponse response = new ExceptionResponse();
        response.setMessage("Internal server error");
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
