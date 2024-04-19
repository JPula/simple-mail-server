package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.exception.UserNotFoundException;
import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserUpdateRequestDto;
import com.rivera.mailservice.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void givenUserExists_whenUserIsRequestedWithId_thenReturnUserInfo() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUserName("betty.boop");
        user.setFirstName("Betty");
        user.setLastName("Boop");
        user.setEmailAddress("betty.boop@email.com");

        when(userService.getUser(1L)).thenReturn(user);

        this.mockMvc
                .perform(get("/user-management/users/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            'id': 1,
                            'userName': 'betty.boop',
                            'firstName': 'Betty',
                            'lastName': 'Boop',
                            'emailAddress': 'betty.boop@email.com',
                            'isDeleted' : false
                        }
                        """));
    }

    @Test
    void givenUserDoesNotExist_whenUserIsRequestedWithId_thenReturnUserNotFound() throws Exception {
        when(userService.getUser(1L)).thenThrow(new UserNotFoundException());

        this.mockMvc
                .perform(get("/user-management/users/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().json("""
                        {
                            'type': 'about:blank',
                            'title': 'Not Found',
                            'status': 404,
                            'detail': 'User not found.',
                            'instance': '/user-management/users/1'
                        }
                        """));
    }

    @Test
    void givenUsersExist_whenAllUsersAreRequested_thenReturnUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("betty.boop");
        user1.setFirstName("Betty");
        user1.setLastName("Boop");
        user1.setEmailAddress("betty.boop@email.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("bugs.bunny");
        user2.setFirstName("Bugs");
        user2.setLastName("Bunny");
        user2.setEmailAddress("bugs.bunny@email.com");

        when(userService.getUsers()).thenReturn(List.of(user1, user2));

        this.mockMvc
                .perform(get("/user-management/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                'id': 1,
                                'userName': 'betty.boop',
                                'firstName': 'Betty',
                                'lastName': 'Boop',
                                'emailAddress': 'betty.boop@email.com',
                                'isDeleted' : false
                            },
                            {
                                'id': 2,
                                'userName': 'bugs.bunny',
                                'firstName': 'Bugs',
                                'lastName': 'Bunny',
                                'emailAddress': 'bugs.bunny@email.com',
                                'isDeleted' : false
                            }
                        ]
                        """));
    }

    @Test
    void givenNoUsersExist_whenAllUsersAreRequested_thenReturnEmptyList() throws Exception {
        when(userService.getUsers()).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(get("/user-management/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenUsersExist_whenUsersAreRequestedWithIds_thenReturnUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUserName("betty.boop");
        user1.setFirstName("Betty");
        user1.setLastName("Boop");
        user1.setEmailAddress("betty.boop@email.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setUserName("bugs.bunny");
        user2.setFirstName("Bugs");
        user2.setLastName("Bunny");
        user2.setEmailAddress("bugs.bunny@email.com");

        when(userService.getUsers(List.of(1L, 2L))).thenReturn(List.of(user1, user2));

        this.mockMvc
                .perform(post("/user-management/users/findByIds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                'id': 1,
                                'userName': 'betty.boop',
                                'firstName': 'Betty',
                                'lastName': 'Boop',
                                'emailAddress': 'betty.boop@email.com',
                                'isDeleted' : false
                            },
                            {
                                'id': 2,
                                'userName': 'bugs.bunny',
                                'firstName': 'Bugs',
                                'lastName': 'Bunny',
                                'emailAddress': 'bugs.bunny@email.com',
                                'isDeleted' : false
                            }
                        ]
                        """));
    }

    @Test
    void givenNoUsersExist_whenUsersAreRequestedWithIds_thenReturnEmptyList() throws Exception {
        when(userService.getUsers(List.of(1L, 2L))).thenReturn(Collections.emptyList());

        this.mockMvc
                .perform(post("/user-management/users/findByIds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void whenDeleteUser_thenDeleteUser() throws Exception {
        doNothing().when(userService).softDelete(1L);

        this.mockMvc
                .perform(delete("/user-management/users/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenDeleteUsers_thenDeleteUsers() throws Exception {
        doNothing().when(userService).softDelete(List.of(1L, 2L));

        this.mockMvc
                .perform(post("/user-management/users/deleteByIds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[1,2]"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void givenExistingUser_whenUpdateUserInfo_thenUpdateUser() throws Exception {
        UserUpdateRequestDto updateRequestDto = new UserUpdateRequestDto();
        updateRequestDto.setFirstName("Daffy");
        updateRequestDto.setLastName("Duck");
        updateRequestDto.setEmailAddress("daffy.duck@email.com");

        User user = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        user.setId(4L);

        when(userService.updateUser(4L, updateRequestDto)).thenReturn(user);

        this.mockMvc
                .perform(patch("/user-management/users/4")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "firstName": "Daffy",
                                    "lastName": "Duck",
                                    "emailAddress": "daffy.duck@email.com"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                                {
                                    'id': 4,
                                    'userName': 'daffy.duck',
                                    'firstName': 'Daffy',
                                    'lastName': "Duck",
                                    'emailAddress': 'daffy.duck@email.com',
                                    'isDeleted': false
                                }
                        """));


    }

}