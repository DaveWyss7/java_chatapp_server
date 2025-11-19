package com.wyssengineeringai.chatserver.exception;

import com.wyssengineeringai.chatserver.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUsernameExists(UsernameAlreadyExistsException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.CONFLICT.value());
        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInvalidCredentials(InvalidCredentialsException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(error));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationErrors(WebExchangeBindException ex) {
        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse error = new ErrorResponse(errors, HttpStatus.BAD_REQUEST.value());
        return Mono.just(ResponseEntity.badRequest().body(error));
    }

}
