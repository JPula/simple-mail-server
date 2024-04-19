package com.rivera.mailservice.mail.controller;

import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail-management")
public class MailController {

    private final MailService mailService;

    @GetMapping("/emails")
    public List<Email> getAllEmails() {
        return mailService.getEmails();
    }
}
