package com.rivera.mailservice.mail.service;

import com.rivera.mailservice.mail.exception.EmailDeliveryException;
import com.rivera.mailservice.mail.model.Email;
import com.rivera.mailservice.mail.util.EmailUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailDeliveryService {

    private final MailService mailService;
    private final JavaMailSender mailSender;

    public Email sendEmail(Email email) {
        try {
            log.debug("[START] Sending email: " + email);
            Email sentEmail = transactionalSendEmail(email);
            log.debug("[END] Sent email: " + email);
            return sentEmail;
        } catch (Exception e) {
            String errorMsg = "Failed to send email to '%s'".formatted(email.getRecipient());
            log.error(errorMsg, e);
            throw new EmailDeliveryException(errorMsg, e);
        }
    }

    @Transactional
    private Email transactionalSendEmail(@Valid Email email) {
        Email savedEmail = mailService.createEmail(email);
        mailSender.send(EmailUtils.toSimpleMailMessage(email));
        return savedEmail;
    }

}
