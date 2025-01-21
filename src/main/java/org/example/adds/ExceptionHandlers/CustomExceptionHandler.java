package org.example.adds.ExceptionHandlers;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@ControllerAdvice
@RequiredArgsConstructor
@Hidden // swagger doc api throws exception
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(LinkExpiredException.class)
    public ResponseEntity<?> handle(LinkExpiredException ex, WebRequest request) {
        log.warn(ex.getMessage());
        return ResponseEntity.status(410).body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handle(NoSuchElementException ex, WebRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
