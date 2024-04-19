package com.rivera.mailservice.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "mail_user")
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_USERNAME_CONSTRAINT", columnNames = "user_name"),
        @UniqueConstraint(name = "UNIQUE_EMAIL_CONSTRAINT", columnNames = "email_address"),
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mail_user_seq")
    @SequenceGenerator(name = "mail_user_seq", sequenceName = "mail_user_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "userName cannot be blank.")
    private String userName;

    @NotBlank(message = "firstName cannot be blank.")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank.")
    private String lastName;

    @NotBlank(message = "emailAddress cannot be blank.")
    @Email(message = "invalid email format.")
    private String emailAddress;

    @JsonProperty("isDeleted")
    private boolean isDeleted = false;

    public User(String username, String firstName, String lastName, String emailAddress) {
        this.userName = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

}
