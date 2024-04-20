package com.rivera.mailservice.mail.service;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.repository.MailRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final MailRepository mailRepository;

    public Email createEmail(Email email) {
        log.debug("[START] Create email: " + email);
        Email savedEmail = mailRepository.save(email);
        log.debug("[END] Created email: " + email);

        return savedEmail;
    }

    public List<Email> getEmails() {
        return mailRepository.findAll();
    }

}
