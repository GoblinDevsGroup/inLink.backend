package org.example.adds.ExceptionHandlers;

import jakarta.servlet.Servlet;

public class PermissionDenied extends RuntimeException {
    public PermissionDenied(String message) {
    }
}