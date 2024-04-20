package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserRegistrationRequestDto;
import com.rivera.mailservice.user.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Registration API")
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @Operation(summary = "Register a user", description = "Registers a user to receive emails")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully registered"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid User Information")
    })
    @PostMapping("/registerUser")
    public User registerUser(@RequestBody UserRegistrationRequestDto userDto) {
        return userRegistrationService.registerUser(userDto.toUser());
    }

}
