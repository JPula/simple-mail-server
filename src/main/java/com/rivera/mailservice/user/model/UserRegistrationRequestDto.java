package com.rivera.mailservice.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(name = "Username", example = "billy.bob", requiredMode = Schema.RequiredMode.REQUIRED)
    private String userName;
    @Schema(name = "Firstname", example = "Billy", requiredMode = Schema.RequiredMode.REQUIRED)
    private String firstName;
    @Schema(name = "Lastname", example = "Bob", requiredMode = Schema.RequiredMode.REQUIRED)
    private String lastName;
    @Schema(name = "Email Address", example = "billy.bob@email.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String emailAddress;

    public User toUser() {
        return new User(userName, firstName, lastName, emailAddress);
    }
}
