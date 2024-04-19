package com.rivera.mailservice.user.service;

import com.rivera.mailservice.user.exception.EmailAddressAlreadyRegisteredException;
import com.rivera.mailservice.user.exception.UsernameTakenException;
import com.rivera.mailservice.user.model.User;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@Transactional
public class UserRegistrationServiceIntegrationTest {

    @Autowired
    UserRegistrationService userRegistrationService;

    @MockBean
    JavaMailSender javaMailSender;


    @Test
    void givenValidUserInfo_whenRegisterUser_thenCreateUser() {
        doNothing().when(javaMailSender);

        User expected = new User("daffy.duck", "Daffy", "Duck", "daffy.duck@email.com");
        User actual = userRegistrationService.registerUser(expected);

        assertNotNull(actual.getId());
        assertEquals(expected.getUserName(), actual.getUserName());
        assertEquals(expected.getEmailAddress(), actual.getEmailAddress());
        assertEquals(expected.isDeleted(), actual.isDeleted());
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoButUsernameAlreadyExists_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop", "Betty", "Boop", "alt-betty.boop@email.com");

        assertThrows(UsernameTakenException.class, () -> {
            userRegistrationService.registerUser(expected);
        });
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoButEmailAlreadyExists_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop2", "Betty", "Boop", "betty.boop@email.com");

        assertThrows(EmailAddressAlreadyRegisteredException.class, () -> {
            userRegistrationService.registerUser(expected);
        });
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoWithMissingUsername_whenRegisterUser_thenThrowException() {
        User expected = new User("", "Betty", "Boop", "alt-betty.boop@email.com");

        TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        e.

        assertEquals(e.getMessage(), "userName cannot be blank.");
    }

}
