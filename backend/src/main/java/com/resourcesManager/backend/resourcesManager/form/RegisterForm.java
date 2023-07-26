package com.resourcesManager.backend.resourcesManager.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {
    @NotBlank(message = "username field can not be blank")
    private String username;
    @NotBlank(message = "password field can not be blank")
    private String password;
    @NotBlank(message = "email field can not be blank")
    @Email(message = "you must use a valid email address")
    private String email;
    @NotBlank(message = "societe field can not be blank")
    private String societe;
}
