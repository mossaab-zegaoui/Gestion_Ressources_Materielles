package com.resourcesManager.backend.resourcesManager.controller;


import com.resourcesManager.backend.resourcesManager.model.Fournisseur;
import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.form.AuthRequest;
import com.resourcesManager.backend.resourcesManager.form.AuthResponse;
import com.resourcesManager.backend.resourcesManager.exceptions.InvalidCredentialsException;
import com.resourcesManager.backend.resourcesManager.event.OnRegistrationCompleteEvent;
import com.resourcesManager.backend.resourcesManager.services.impl.AuthenticationServiceImpl;
import com.resourcesManager.backend.resourcesManager.services.impl.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class AuthController {
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationServiceImpl authenticationService;
    private final LogoutService logoutService;

    @GetMapping
    public List<User> getUsers() {
        return authenticationService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable String userId) {
        return authenticationService.getUserById(userId);
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return authenticationService.saveUser(user);
    }


    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return authenticationService.getAllRoles();
    }

    @PutMapping("/{userId}")
    public User updateUserById(@PathVariable String userId, @RequestBody User user) {
        user.setId(userId);
        return authenticationService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        authenticationService.deleteUser(userId);
    }

    @PostMapping("/register")
    public Fournisseur register(@RequestBody @Valid Fournisseur fournisseur, final HttpServletRequest request) {

        String appPort = "4200";
        Fournisseur savedFournisseur = authenticationService.registerFournisseur(fournisseur);
//        String appURL = "http://" + request.getServerName() + ":" + appPort + request.getContextPath();
//        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(savedFournisseur, request.getLocale(), appURL));

        return savedFournisseur;
    }

    @PostMapping("/authenticate")
    public AuthResponse authenticate(@RequestBody AuthRequest request) throws InvalidCredentialsException {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }

    @PostMapping("/refreshToken")
    public AuthResponse refreshToken(HttpServletRequest request) {
        return authenticationService.refreshToken(request);
    }

}
