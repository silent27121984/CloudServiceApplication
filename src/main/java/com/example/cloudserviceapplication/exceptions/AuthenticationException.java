package com.example.cloudserviceapplication.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("User is not authorized");
    }
}
