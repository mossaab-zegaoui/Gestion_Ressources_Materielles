package com.resourcesManager.backend.resourcesManager.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordForm {
    @NotBlank(message = "token field can not be empty")
    private String token;
    @NotBlank(message = "New Password field can not be empty")
    private String newPassword;
    @NotBlank(message = "confirm Password  field can not be empty")
    private String confirmPassword;

}
