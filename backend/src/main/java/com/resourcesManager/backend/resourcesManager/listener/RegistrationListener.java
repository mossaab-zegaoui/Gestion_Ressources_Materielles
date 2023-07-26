package com.resourcesManager.backend.resourcesManager.listener;

import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.event.OnRegistrationCompleteEvent;
import com.resourcesManager.backend.resourcesManager.services.impl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final AuthenticationServiceImpl authenticationService;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        authenticationService.createVerificationToken(user, token);
// Send a verification email
        String recipientEmail = user.getEmail();
        String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        log.info("confirmation url : {}", confirmationUrl);

//        sendVerificationEmail(recipientEmail, confirmationUrl);
    }

    void sendVerificationEmail(String recipientEmail, String confirmationUrl) {
        SimpleMailMessage email = new SimpleMailMessage();
        String subject = "Registration Confirmation";
        email.setFrom(fromEmail);
        email.setTo(recipientEmail);
        email.setSubject(subject);
        email.setText("Thank you for registering with us" + "\r\n" + "Please, click the link below to complete your registration" +
                "\r\n" + confirmationUrl);
        mailSender.send(email);
    }

    public void sendResetEmail(String url, String token, User user) {
        String resetURL = url + "/validateResetPassword?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        String subject = "Reset Password";
        email.setFrom(fromEmail);
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText("To Reset your password " + "\r\n" + "Please, click  the link below" + "\r\n" + resetURL);
        log.info("reset password url: {}", resetURL);
        mailSender.send(email);
    }

    public void sendPasswordUpdated(User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        String subject = "Password updated";
        email.setFrom(fromEmail);
        email.setTo(user.getEmail());
        email.setSubject(subject);
        email.setText("your password has been updated successfully");
        log.info("email: {} has updated his password ", user.getEmail());
//        mailSender.send(email);
    }
}
