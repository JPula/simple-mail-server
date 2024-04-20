package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserUpdateRequestDto;
import com.rivera.mailservice.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-management")
@Tag(name = "User API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Request user by Id", description = "Returns user based on Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
            @ApiResponse(responseCode = "404", description = "Not Found - User not Found")
    })
    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) throws Exception {
        return userService.getUser(id);
    }

    @Operation(summary = "Delete user by Id", description = "Deletes user based on Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
            @ApiResponse(responseCode = "404", description = "Not Found - User not Found")
    })
    @DeleteMapping("/users/{id}")
    public void softDeleteUser(@PathVariable("id") Long id) {
        userService.softDelete(id);
    }

    @Operation(summary = "Update user by Id", description = "Updates user using info in request body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request - Invalid User Info")
    })
    @PatchMapping("/users/{id}")
    public User updateUser(@PathVariable("id") Long id,
                           @RequestBody UserUpdateRequestDto updateRequestDto) {
        return userService.updateUser(id, updateRequestDto);
    }

    @Operation(summary = "Request all Users", description = "Return all Users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
    })
    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Request users by Ids", description = "Return Users found with Ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned"),
    })
    @PostMapping("/users/findByIds")
    public List<User> getUsers(@RequestBody List<Long> userIds) {
        return userService.getUsers(userIds);
    }

    @Operation(summary = "Delete users by Ids", description = "Delete users found with Ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted"),
    })
    @PostMapping("/users/deleteByIds")
    public void softDeleteUser(@RequestBody List<Long> ids) {
        userService.softDelete(ids);
    }


}
