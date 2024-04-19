package com.rivera.mailservice.user.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Optional;

@Data
@NoArgsConstructor
public class UserUpdateRequestDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 7824403593914188662L;

    private String firstName;
    private String lastName;
    private String emailAddress;

    public User updateUser(User user) {
        Optional.ofNullable(firstName).ifPresent(user::setFirstName);
        Optional.ofNullable(lastName).ifPresent(user::setLastName);
        Optional.ofNullable(emailAddress).ifPresent(user::setEmailAddress);
        return user;
    }
}
