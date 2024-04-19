package com.rivera.mailservice.user.exception;

import jakarta.validation.ConstraintViolationException;

public class UsernameTakenException extends ConstraintViolationException {
    private static final String message = "Username is already taken.";

    public UsernameTakenException() {
        super(message, null);
    }

}
