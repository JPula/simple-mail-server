package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.exception.UserNotFoundException;
import com.rivera.mailservice.user.model.User;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        UserController.class,
        UserRegistrationController.class
})
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity
                .of(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "User not found."))
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder errorMessage = new StringBuilder();
        e.getConstraintViolations().forEach(
                v -> errorMessage.append(v.getMessage() + "; "));

        return ResponseEntity
                .of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage.toString()))
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String errorMessage = e.getMessage();
        if (errorMessage.contains(User.Constraint.UNIQUE_USERNAME_CONSTRAINT)) {
            errorMessage = "username is already taken.";
        } else if (errorMessage.contains(User.Constraint.UNIQUE_EMAIL_CONSTRAINT)) {
            errorMessage = "email address is already registered.";
        }

        return ResponseEntity
                .of(ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage))
                .build();
    }
}
