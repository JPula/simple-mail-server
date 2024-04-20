package com.rivera.mailservice.user.service;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.service.MailDeliveryService;
import com.rivera.mailservice.user.model.User;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TransactionManagementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final MailDeliveryService mailDeliveryService;

    public User registerUser(@Valid User user) {
        try {
            return registerUserWithTransaction(user);
        } catch (TransactionManagementException e) {
            if (e.getCause() != null
                    &&  e.getCause().getCause() != null
                    && e.getCause().getCause() instanceof ConstraintViolationException) {
                throw (ConstraintViolationException) e.getCause().getCause();
            } else {
                throw e;
            }
        }
    }

    @Transactional
    private User registerUserWithTransaction(User user) {
        User registeredUser = userService.createUser(user);
        mailDeliveryService.sendEmail(createWelcomeEmail(user));

        return registeredUser;
    }

    private Email createWelcomeEmail(User user) {
        Email email = new Email();
        email.setUserId(user.getId());
        email.setSender("no-reply@mailserver.com");
        email.setRecipient(user.getEmailAddress());
        email.setSubject("Welcome %s!".formatted(user.getUserName()));
        email.setText("Thanks for Subscribing!");
        return email;
    }

}
