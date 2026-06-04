package com.devgui.urlshortener.api.exception;

import com.devgui.urlshortener.domain.exception.ShortUrlInactiveException;
import com.devgui.urlshortener.domain.exception.ShortUrlNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed for one or more fields.";

    @ExceptionHandler(ShortUrlNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShortUrlNotFoundException(ShortUrlNotFoundException e, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ShortUrlInactiveException.class)
    public ResponseEntity<ErrorResponse> handleShortUrlInactiveException(ShortUrlInactiveException e, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.NOT_FOUND, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){
        List<FieldErrorDetail> details = e.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> new FieldErrorDetail(f.getField(), f.getDefaultMessage()))
                .toList();
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, request.getRequestURI(), details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            HandlerMethodValidationException e,
            HttpServletRequest request) {

        List<FieldErrorDetail> details = e.getParameterValidationResults()
                .stream()
                .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> new FieldErrorDetail(
                                result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage()
                        )))
                .toList();

        ErrorResponse error = ErrorResponse.of(
                HttpStatus.BAD_REQUEST, VALIDATION_FAILED_MESSAGE, request.getRequestURI(), details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e, HttpServletRequest request){
        ErrorResponse error = ErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
