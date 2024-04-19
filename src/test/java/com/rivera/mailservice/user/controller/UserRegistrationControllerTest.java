package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.exception.EmailAddressAlreadyRegisteredException;
import com.rivera.mailservice.user.exception.UsernameTakenException;
import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.service.UserRegistrationService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRegistrationController.class)
public class UserRegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserRegistrationService userRegistrationService;

    @Test
    void whenRegisterUser_thenCreateAndReturnNewUser() throws Exception {
        User user = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        User userWithId = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        userWithId.setId(4L);

        when(userRegistrationService.registerUser(user)).thenReturn(userWithId);

        this.mockMvc
                .perform(post("/user-management/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userName": "daffy.duck",
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
                                    "firstName": "Daffy",
                                    "lastName": "Duck",
                                    'emailAddress': 'daffy.duck@email.com',
                                    'isDeleted': false
                                }
                        """));

    }

    @Test
    void givenUser_whenRegisterUserWithSameUsername_thenThrowException() throws Exception {
        User user = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        when(userRegistrationService.registerUser(user)).thenThrow(new UsernameTakenException());

        this.mockMvc
                .perform(post("/user-management/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userName": "daffy.duck",
                                    "firstName": "Daffy",
                                    "lastName": "Duck",
                                    "emailAddress": "daffy.duck@email.com"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                            'type': 'about:blank',
                            'title': 'Bad Request',
                            'status': 400,
                            'detail': 'Username is already taken.',
                            'instance': '/user-management/registerUser'
                        }
                        """));

    }

    @Test
    void givenUser_whenRegisterUserWithSameEmail_thenThrowException() throws Exception {
        User user = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        when(userRegistrationService.registerUser(user)).thenThrow(new EmailAddressAlreadyRegisteredException());

        this.mockMvc
                .perform(post("/user-management/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userName": "daffy.duck",
                                    "firstName": "Daffy",
                                    "lastName": "Duck",
                                    "emailAddress": "daffy.duck@email.com"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                            'type': 'about:blank',
                            'title': 'Bad Request',
                            'status': 400,
                            'detail': 'Email Address already registered.',
                            'instance': '/user-management/registerUser'
                        }
                        """));

    }

    @Test
    void whenRegisterInvalidUserInfo_thenThrowException() throws Exception {
        User user = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        user.setUserName("");

        when(userRegistrationService.registerUser(user))
                .thenThrow(new ConstraintViolationException("Constraint was violated.", null));

        this.mockMvc
                .perform(post("/user-management/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userName": "",
                                    "firstName": "Daffy",
                                    "lastName": "Duck",
                                    "emailAddress": "daffy.duck@email.com"
                                }
                                """))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        {
                            'type': 'about:blank',
                            'title': 'Bad Request',
                            'status': 400,
                            'detail': 'Constraint was violated.',
                            'instance': '/user-management/registerUser'
                        }
                        """));

    }
}
