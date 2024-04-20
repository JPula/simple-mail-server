package com.rivera.mailservice.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "Firstname", example = "Billy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String firstName;
    @Schema(name = "Lastname", example = "Bob", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String lastName;
    @Schema(name = "Email Address", example = "billy.bob@email.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String emailAddress;

    public User updateUser(User user) {
        Optional.ofNullable(firstName).ifPresent(user::setFirstName);
        Optional.ofNullable(lastName).ifPresent(user::setLastName);
        Optional.ofNullable(emailAddress).ifPresent(user::setEmailAddress);
        return user;
    }
}
