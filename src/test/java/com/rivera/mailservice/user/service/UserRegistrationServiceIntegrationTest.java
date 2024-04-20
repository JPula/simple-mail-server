package com.rivera.mailservice.user.service;

import com.rivera.mailservice.user.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

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

        DataIntegrityViolationException e = assertThrows(DataIntegrityViolationException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        assertTrue(e.getMessage().contains(User.Constraint.UNIQUE_USERNAME_CONSTRAINT));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoButEmailAlreadyExists_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop2", "Betty", "Boop", "betty.boop@email.com");

        DataIntegrityViolationException e = assertThrows(DataIntegrityViolationException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        assertTrue(e.getMessage().contains(User.Constraint.UNIQUE_EMAIL_CONSTRAINT));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoWithMissingUsername_whenRegisterUser_thenThrowException() {
        User expected = new User("", "Betty", "Boop", "alt-betty.boop@email.com");

        TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getMostSpecificCause();
        List<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations().stream().toList();

        assertEquals(constraintViolations.get(0).getMessage(), "userName cannot be blank.");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoWithMissingFirstName_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop", "", "Boop", "alt-betty.boop@email.com");

        TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getMostSpecificCause();
        List<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations().stream().toList();

        assertEquals(constraintViolations.get(0).getMessage(), "firstName cannot be blank.");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoWithMissingLastName_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop", "Betty", "", "alt-betty.boop@email.com");

        TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getMostSpecificCause();
        List<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations().stream().toList();

        assertEquals(constraintViolations.get(0).getMessage(), "lastName cannot be blank.");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoWithMissingEmail_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop", "Betty", "Boop", "");

        TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getMostSpecificCause();
        List<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations().stream().toList();

        assertEquals(constraintViolations.get(0).getMessage(), "emailAddress cannot be blank.");
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void givenUserInfoWithInvalidEmail_whenRegisterUser_thenThrowException() {
        User expected = new User("betty.boop", "Betty", "Boop", "betty");

        TransactionSystemException e = assertThrows(TransactionSystemException.class, () -> {
            userRegistrationService.registerUser(expected);
        });

        ConstraintViolationException constraintViolationException = (ConstraintViolationException) e.getMostSpecificCause();
        List<ConstraintViolation<?>> constraintViolations = constraintViolationException.getConstraintViolations().stream().toList();

        assertEquals(constraintViolations.get(0).getMessage(), "invalid email format.");
    }

}
