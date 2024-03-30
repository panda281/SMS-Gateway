package com.meinab.smsgateway.exceptions;

import com.meinab.smsgateway.dto.ErrorMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorMessageDto handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder errorMessages = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorMessages.append(fieldName).append(" ").append(errorMessage).append("\n");
        });
        return ErrorMessageDto.builder().message(errorMessages.toString()).build();
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErrorMessageDto> handleUserNotFoundException(UserNotFound exception) {
        ErrorMessageDto errorMessage = ErrorMessageDto.builder().message(exception.getMessage()).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorMessageDto> handleRuntimeException(RuntimeException exception) {
        if(exception instanceof HttpMessageNotReadableException)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorMessageDto.builder().message("Incorrect body").build());
        log.error(exception.getMessage(), exception);
        ErrorMessageDto errorMessage = ErrorMessageDto.builder().message("UnExpected Error occurred please try again later").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageDto> handleException(Exception exception) {
        if(exception instanceof HttpRequestMethodNotSupportedException)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageDto.builder().message("Endpoint not found").build());
        else if (exception instanceof NoResourceFoundException)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorMessageDto.builder().message("Resource not found").build());
        log.error(exception.getMessage(), exception);
        ErrorMessageDto errorMessage = ErrorMessageDto.builder().message("UnExpected Error occurred please try again later").build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }
}
