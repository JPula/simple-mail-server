package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserRegistrationRequestDto;
import com.rivera.mailservice.user.service.UserRegistrationService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-management")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PostMapping("/registerUser")
    public User registerUser(@RequestBody UserRegistrationRequestDto userDto) {
        return userRegistrationService.registerUser(userDto.toUser());
    }

}
