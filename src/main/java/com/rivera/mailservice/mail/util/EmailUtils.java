package com.rivera.mailservice.mail.util;

import com.rivera.mailservice.mail.model.Email;
import org.springframework.mail.SimpleMailMessage;

public abstract class EmailUtils {
    public static SimpleMailMessage toSimpleMailMessage(Email email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email.getSender());
        message.setTo(email.getRecipient());
        message.setSubject(email.getSubject());
        message.setText(email.getText());

        return message;
    }

}
