package com.sgz.TodoApp.exceptions;

public class InvalidAuthorityException extends Exception {

    public InvalidAuthorityException(String message) {
        super(message);
    }

    public InvalidAuthorityException(String message, Throwable cause) {
        super(message, cause);
    }

}
