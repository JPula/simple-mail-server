package com.rivera.mailservice.mail.controller;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.service.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MailController.class)
class MailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MailService mailService;

    @Test
    void givenUsersExist_whenAllUsersAreRequested_thenReturnUsers() throws Exception {
        Email email1 = new Email();
        email1.setId(1L);
        email1.setSender("no-reply@mailserver.com");
        email1.setRecipient("betty.boop@email.com");
        email1.setSubject("Test Subject 1");
        email1.setText("Test Text 1");

        Email email2 = new Email();
        email2.setId(2L);
        email2.setSender("no-reply@mailserver.com");
        email2.setRecipient("charlie.brown@email.com");
        email2.setSubject("Test Subject 2");
        email2.setText("Test Text 2");

        when(mailService.getEmails()).thenReturn(List.of(email1, email2));

        this.mockMvc
                .perform(get("/mail-management/emails"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                                'id': 1,
                                'sender': 'no-reply@mailserver.com',
                                'recipient': 'betty.boop@email.com',
                                'subject': 'Test Subject 1',
                                'text': 'Test Text 1'
                            },
                            {
                                'id': 2,
                                'sender': 'no-reply@mailserver.com',
                                'recipient': 'charlie.brown@email.com',
                                'subject': 'Test Subject 2',
                                'text': 'Test Text 2'
                            }
                        ]
                        """));
    }

}