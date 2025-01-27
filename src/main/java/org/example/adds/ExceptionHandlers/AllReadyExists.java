package org.example.adds.ExceptionHandlers;

public class AllReadyExists extends RuntimeException {
    public AllReadyExists(String message) {
        super(message);
    }
}
