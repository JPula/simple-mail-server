package com.rivera.mailservice.user.model;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@NoArgsConstructor
public class UserRegistrationRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1636815541190260734L;

    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;

    public User toUser() {
        return new User(userName, firstName, lastName, emailAddress);
    }
}
