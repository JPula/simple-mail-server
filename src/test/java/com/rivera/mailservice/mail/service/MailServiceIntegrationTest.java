package com.rivera.mailservice.mail.service;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MailServiceIntegrationTest {

    @Autowired
    private MailService mailService;

    @Test
    void given3Emails_whenGetAllEmails_thenReturn3Emails() {
        List<Email> emails = mailService.getEmails();
        assertEquals(3, emails.size());
    }

}