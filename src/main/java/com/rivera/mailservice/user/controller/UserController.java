package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserUpdateRequestDto;
import com.rivera.mailservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-management")
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") Long id) throws Exception {
        return userService.getUser(id);
    }

    @DeleteMapping("/users/{id}")
    public void softDeleteUser(@PathVariable("id") Long id) {
        userService.softDelete(id);
    }

    @PatchMapping("/users/{id}")
    public User updateUser(@PathVariable("id") Long id,
                           @RequestBody UserUpdateRequestDto updateRequestDto) {
        return userService.updateUser(id, updateRequestDto);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/users/findByIds")
    public List<User> getUsers(@RequestBody List<Long> userIds) {
        return userService.getUsers(userIds);
    }

    @PostMapping("/users/deleteByIds")
    public void softDeleteUser(@RequestBody List<Long> ids) {
        userService.softDelete(ids);
    }


}
