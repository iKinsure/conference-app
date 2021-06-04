package com.ikinsure.conference.user.dto;

import javax.validation.constraints.*;

/**
 * DTO class
 */
public class UserCommand {

    @NotNull(message = "{email.not-empty}")
    @NotEmpty(message = "{email.not-empty}")
    @NotBlank(message = "{email.not-empty}")
    @Email(message = "{email.not-valid}")
    private String email;

    @NotNull(message = "{login.not-empty}")
    @NotEmpty(message = "{login.not-empty}")
    @NotBlank(message = "{login.not-empty}")
    @Size(min = 5, max = 32, message = "{login.not-valid}")
    private String login;

    public UserCommand() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserCreateCommand{" +
                "email='" + email + '\'' +
                ", login='" + login + '\'' +
                '}';
    }
}
