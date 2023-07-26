package com.resourcesManager.backend.resourcesManager.form;

import lombok.Data;

@Data
public class PasswordDTO {
    private String newPassword;
    private String oldPassword;
    private String email;
    private String token;
}
