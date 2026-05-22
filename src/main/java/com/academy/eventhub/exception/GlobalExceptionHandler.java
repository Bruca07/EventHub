package com.academy.eventhub.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- GESTIONE ERRORI 404 (Not Found) ---
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException exc) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            exc.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    } 

    // --- GESTIONE ERRORI 403 (Forbidden) ---
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException exc) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            "Non hai i permessi necessari per accedere a questa risorsa."
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // GESTIONE ERRORI 400 (Bad Request ) 
    @ExceptionHandler(ValidationException.class)
public ResponseEntity<ErrorResponse> handleBadRequest(ValidationException exc) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Bad Request",
        exc.getMessage()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}

    // --- GESTIONE ERRORI 400 (Bad Request - Validazione DTO @Valid) ---
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exc) {
        String errorMessage = exc.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));
                
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Bad Request",
            errorMessage
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // --- GESTIONE ERRORI 409 (Conflict - Per i duplicati) ---
@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<ErrorResponse> handleConflict(IllegalArgumentException exc) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(), // Qui imposto lo status a 409
        "Conflict",
        exc.getMessage()
    );
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
}
}