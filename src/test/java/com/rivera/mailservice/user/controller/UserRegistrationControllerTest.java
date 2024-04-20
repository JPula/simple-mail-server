package com.rivera.mailservice.user.controller;

import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.service.UserRegistrationService;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.TransactionManagementException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Set;

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

        when(userRegistrationService.registerUser(user))
                .thenThrow(new DataIntegrityViolationException(User.Constraint.UNIQUE_USERNAME_CONSTRAINT));

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
                            'detail': 'username is already taken.',
                            'instance': '/user-management/registerUser'
                        }
                        """));

    }

    @Test
    void givenUser_whenRegisterUserWithSameEmail_thenThrowException() throws Exception {
        User user = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        when(userRegistrationService.registerUser(user))
                .thenThrow(new DataIntegrityViolationException(User.Constraint.UNIQUE_EMAIL_CONSTRAINT));

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
                            'detail': 'email address is already registered.',
                            'instance': '/user-management/registerUser'
                        }
                        """));

    }

    @Test
    void whenRegisterInvalidUserInfo_thenThrowException() throws Exception {
        User user = new User("", "", "Duck", "daffy.duck@email.com");

        when(userRegistrationService.registerUser(user)).thenThrow(new ConstraintViolationException(Set.of(
                ConstraintViolationImpl.forBeanValidation(
                        null,
                        null,
                        null,
                        "userName cannot be blank.",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                ConstraintViolationImpl.forBeanValidation(
                        null,
                        null,
                        null,
                        "firstName cannot be blank.",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                )

        )));

        this.mockMvc
                .perform(post("/user-management/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "userName": "",
                                    "firstName": "",
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
                            'detail': 'userName cannot be blank.; firstName cannot be blank.; ',
                            'instance': '/user-management/registerUser'
                        }
                        """));

    }
}
