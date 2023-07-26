package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.token.VerificationToken;
import com.resourcesManager.backend.resourcesManager.model.Fournisseur;
import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.form.AuthRequest;
import com.resourcesManager.backend.resourcesManager.form.AuthResponse;
import com.resourcesManager.backend.resourcesManager.exceptions.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AuthenticationService {
    User getUserByUserName(String userName);

    User getUserById(String userId);

    User saveUser(User user);

    void deleteUser(String userId);

    AuthResponse authenticate(AuthRequest request) throws InvalidCredentialsException;

    User saveResponsable(User user);

    Fournisseur registerFournisseur(Fournisseur fournisseur);

    User updateUser(User user);

    Role saveRole(String roleName);

    Role getRole(String roleName);

    List<User> getAllUsers();

    List<Role> getAllRoles();

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String verificationToken);


}
