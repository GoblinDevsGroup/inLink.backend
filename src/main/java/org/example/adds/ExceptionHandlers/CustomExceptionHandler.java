package org.example.adds.ExceptionHandlers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.adds.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
@Hidden // swagger doc api throws exception
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<?> handle(LinkExpiredException ex, WebRequest request) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(410)
                .body(new Response(ex.getMessage(), false));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(NoSuchElementException ex, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(404)
                .body(new Response(ex.getMessage(), false));
    }

    @ExceptionHandler(AllReadyExists.class)
    public ResponseEntity<?> handle(AllReadyExists ex, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(409)
                .body(new Response(ex.getMessage(), false));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response("The phone number is already in use. Please use a different number.", false));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> ((FieldError) error).getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(errors, false));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(ex.getMessage(), false));
    }

    @ExceptionHandler(PermissionDenied.class)
    public ResponseEntity<Response> handlePermissionDeniedException(PermissionDenied ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(ex.getMessage(), false));
    }
}
