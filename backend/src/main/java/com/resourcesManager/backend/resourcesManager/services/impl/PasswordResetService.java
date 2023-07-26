package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.model.token.VerificationToken;
import com.resourcesManager.backend.resourcesManager.exceptions.ApiException;
import com.resourcesManager.backend.resourcesManager.exceptions.InvalidCredentialsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.exceptions.TokenExpiredException;
import com.resourcesManager.backend.resourcesManager.repository.UserRepository;
import com.resourcesManager.backend.resourcesManager.repository.VerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static com.resourcesManager.backend.resourcesManager.model.token.TokenType.PASSWORD_RESET_TOKEN;

@Service
@Slf4j
public class PasswordResetService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository resetPasswordRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    @Value("${spring.mail.username}")
    private String fromEmail;


    public PasswordResetService(UserRepository userRepository,
                                VerificationTokenRepository resetPasswordRepository,
                                JavaMailSender mailSender,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resetPasswordRepository = resetPasswordRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new NotFoundException("user with email\n " + email + "\n\nnot found"));
        return user;
    }

    public void createPasswordResetToken(User user, String token) {
        VerificationToken resetPasswordToken = new VerificationToken(user, token, PASSWORD_RESET_TOKEN);
        resetPasswordRepository.save(resetPasswordToken);
    }


    public User getUserByVerificationToken(String token) {
        Optional<VerificationToken> resetPasswordToken = resetPasswordRepository.findByToken(token);

        return resetPasswordToken.map(VerificationToken::getUser)
                .orElseThrow(() -> new NotFoundException("no user found for the verification token: " + token));

    }

    public void validateResetToken(String token) {
        VerificationToken resetToken = resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("reset token: " + token + " not found"));
        final Calendar calendar = Calendar.getInstance();
        if (resetToken.getExpirationDate().before(calendar.getTime()))
            throw new TokenExpiredException("token" + token + "has expired");

    }

    public void updateUserPassword(User user, String password, String confirmPassword) {
        if (!password.equals(confirmPassword))
            throw new ApiException("two password must be the same");
        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        userRepository.save(user);
        log.info("user {} has changed his password", user.getEmail());

    }

    public boolean oldPasswordIsValid(User user, String oldPassword) {

        if (!passwordEncoder.matches(user.getPassword(), oldPassword))
            throw new InvalidCredentialsException("wrong credentials");
        return true;
    }
}
