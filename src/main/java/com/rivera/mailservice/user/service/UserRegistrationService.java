package com.rivera.mailservice.user.service;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.service.MailDeliveryService;
import com.rivera.mailservice.user.exception.EmailAddressAlreadyRegisteredException;
import com.rivera.mailservice.user.exception.UsernameTakenException;
import com.rivera.mailservice.user.model.User;
import com.rivera.mailservice.user.model.UserConstraint;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final UserService userService;
    private final MailDeliveryService mailDeliveryService;

    public User registerUser(User user) {
        try {
            return registerUserWithTransaction(user);
        } catch (ConstraintViolationException e)  {
            if (e.getMessage().contains(UserConstraint.UNIQUE_USERNAME_CONSTRAINT.name())) {
                throw new UsernameTakenException();
            } else if (e.getMessage().contains(UserConstraint.UNIQUE_EMAIL_CONSTRAINT.name())) {
                throw new EmailAddressAlreadyRegisteredException();
            } else {
                throw  e;
            }
        }
    }

    @Transactional
    private User registerUserWithTransaction(@Valid User user) {
        User registeredUser = userService.createUser(user);
        mailDeliveryService.sendEmail(createWelcomeEmail(user));

        return registeredUser;
    }

    private Email createWelcomeEmail(User user) {
        Email email = new Email();
        email.setSender("no-reply@mailserver.com");
        email.setRecipient(user.getEmailAddress());
        email.setSubject("Welcome %s!".formatted(user.getUserName()));
        email.setText("Thanks for Subscribing!");
        return email;
    }

}
