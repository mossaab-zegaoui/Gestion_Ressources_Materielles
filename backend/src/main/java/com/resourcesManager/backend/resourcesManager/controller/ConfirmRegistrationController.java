package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.form.PasswordDTO;
import com.resourcesManager.backend.resourcesManager.model.token.VerificationToken;
import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.exceptions.TokenExpiredException;
import com.resourcesManager.backend.resourcesManager.form.ResetPasswordForm;
import com.resourcesManager.backend.resourcesManager.listener.RegistrationListener;
import com.resourcesManager.backend.resourcesManager.services.impl.AuthenticationServiceImpl;
import com.resourcesManager.backend.resourcesManager.services.impl.PasswordResetService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class ConfirmRegistrationController {
    private final AuthenticationServiceImpl authenticationService;
    private final PasswordResetService passwordResetService;
    private final RegistrationListener eventListener;

    @GetMapping("registrationConfirm")
    public boolean confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = authenticationService.getVerificationToken(token);
        User user = verificationToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((verificationToken.getExpirationDate().getTime() - calendar.getTime().getTime()) <= 0)
            throw new TokenExpiredException("token has been expired");
        user.setEnabled(true);
        authenticationService.updateUser(user);
        return true;
    }

    @PostMapping("resetPassword")
    public void resetPassword(HttpServletRequest request, @RequestParam("email") String email) {
        String port = "4200";
        User user = passwordResetService.getUserByEmail(email);
        String token = UUID.randomUUID().toString();
        passwordResetService.createPasswordResetToken(user, token);
        String appURL = "http://" + request.getServerName() + ":" + port + request.getContextPath();
        eventListener.sendResetEmail(appURL, token, user);
    }

    @GetMapping("validateResetPassword")
    public void validateResetPassword(@RequestParam("token") String token) {
        passwordResetService.validateResetToken(token);
    }

    @PostMapping("changePassword")
    private void changePassword(@RequestBody @Valid ResetPasswordForm form) {

        User user = passwordResetService.getUserByVerificationToken(form.getToken());
        passwordResetService.updateUserPassword(user, form.getNewPassword(), form.getConfirmPassword());

    }

    @PostMapping("updatePassword")
    public void updatePassword(@RequestBody PasswordDTO passwordDTO) {
        User user = passwordResetService.getUserByEmail(passwordDTO.getEmail());
        if (passwordResetService.oldPasswordIsValid(user, passwordDTO.getOldPassword()))
            eventListener.sendPasswordUpdated(user);

    }

}
