package com.rivera.mailservice.user.exception;

import jakarta.validation.ConstraintViolationException;

public class EmailAddressAlreadyRegisteredException extends ConstraintViolationException {
    private static final String message = "Email Address already registered.";

    public EmailAddressAlreadyRegisteredException() {
        super(message, null);
    }

}
