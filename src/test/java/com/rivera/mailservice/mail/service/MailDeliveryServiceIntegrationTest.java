package com.rivera.mailservice.mail.service;

import com.rivera.mailservice.mail.exception.EmailDeliveryException;
import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.util.EmailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Transactional
class MailDeliveryServiceIntegrationTest {

    @Autowired
    MailDeliveryService mailDeliveryService;

    @Autowired
    MailService mailService;

    @MockBean
    JavaMailSender mockMailSender;

    @Test
    public void givenEmail_whenSendEmail_shouldCreateEmailRecord() {
        Email email = new Email();
        email.setUserId(4L);
        email.setRecipient("daffy.duck@email.com");
        email.setSender("no-reply@mailserver.com");
        email.setSubject("Test Subject");
        email.setText("Test Body");

        doNothing().when(mockMailSender);

        Email savedEmail = mailDeliveryService.sendEmail(email);

        assertEquals(4L, savedEmail.getId());
        assertEquals(email.getUserId(), savedEmail.getUserId());
        assertEquals(email.getRecipient(), savedEmail.getRecipient());
        assertEquals(email.getSender(), savedEmail.getSender());
        assertEquals(email.getSubject(), savedEmail.getSubject());
        assertEquals(email.getText(), savedEmail.getText());
    }

    @Test
    @DirtiesContext
    public void whenSendEmailFails_shouldThrowExceptionAndRollbackTransaction() {
        Email email = new Email();
        email.setUserId(1L);
        email.setRecipient("daffy.duck@email.com");
        email.setSender("no-reply@mailserver.com");
        email.setSubject("Test Subject");
        email.setText("Test Body");

        EmailDeliveryException e = assertThrows(EmailDeliveryException.class, () -> {
            doThrow(new MailSendException("Failed")).when(mockMailSender)
                    .send(any(SimpleMailMessage.class));

            mailDeliveryService.sendEmail(email);
        });

        assertEquals("Failed to send email to 'daffy.duck@email.com'", e.getMessage());
        assertTrue(TestTransaction.isFlaggedForRollback());
    }


}