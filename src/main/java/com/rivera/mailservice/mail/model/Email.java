package com.rivera.mailservice.mail.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.rivera.mailservice.user.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 8559863072111608509L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_seq")
    @SequenceGenerator(name = "email_seq", sequenceName = "email_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "userId cannot be null")
    private Long userId;

    @NotBlank(message = "recipient cannot be blank.")
    @jakarta.validation.constraints.Email(message = "invalid email format")
    private String recipient;

    @NotBlank(message = "sender cannot be blank.")
    @jakarta.validation.constraints.Email(message = "invalid email format")
    private String sender;

    @NotBlank(message = "subject cannot be blank.")
    private String subject;

    @NotBlank(message = "text cannot be blank.")
    private String text;
}
