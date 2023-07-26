package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.enums.RoleName;
import com.resourcesManager.backend.resourcesManager.model.token.VerificationToken;
import com.resourcesManager.backend.resourcesManager.model.Fournisseur;
import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.form.AuthRequest;
import com.resourcesManager.backend.resourcesManager.form.AuthResponse;
import com.resourcesManager.backend.resourcesManager.model.token.Token;
import com.resourcesManager.backend.resourcesManager.model.token.TokenType;
import com.resourcesManager.backend.resourcesManager.exceptions.AccountBannedException;
import com.resourcesManager.backend.resourcesManager.exceptions.EntityAlreadyExistsException;
import com.resourcesManager.backend.resourcesManager.exceptions.InvalidCredentialsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.*;
import com.resourcesManager.backend.resourcesManager.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.resourcesManager.backend.resourcesManager.enums.RoleName.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final RoleRepository roleRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final FournisseurRepository fournisseurRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }


    @Override
    public User getUserByUserName(String userName) {
        User user = userRepository.findUserByUsername(userName)
                .orElseThrow(() -> new NotFoundException("user " + userName + " NOT FOUND !!! "));

        return user;
    }

    @Override
    public User getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user with id: " + userId + " NOT FOUND !!! "));

        return user;
    }

    @Override
    public User saveUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new EntityAlreadyExistsException("Usename:" + user.getUsername() + " already exists");
        if (userRepository.existsByEmailIgnoreCase(user.getEmail()))
            throw new EntityAlreadyExistsException("email:" + user.getEmail() + " already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = getRole(ROLE_USER.name());
        user.setRoles(List.of(role));
        return userRepository.save(user);
    }

    @Override
    public User saveResponsable(User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            throw new EntityAlreadyExistsException("Usename:" + user.getUsername() + " already exists");

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = getRole(ROLE_RESPONSABLE.name());
        user.setRoles(List.of(role));
        return userRepository.save(user);
    }

    @Override
    public Fournisseur registerFournisseur(Fournisseur fournisseur) {
        if (userRepository.existsByUsername(fournisseur.getUsername()))
            throw new EntityAlreadyExistsException("username:" + fournisseur.getUsername() + " already exists");
        if (userRepository.existsByEmailIgnoreCase(fournisseur.getEmail()))
            throw new EntityAlreadyExistsException("email:" + fournisseur.getEmail() + " already exists");

        fournisseur.setEnabled(false);
        fournisseur.setPassword(passwordEncoder.encode(fournisseur.getPassword()));
        Role role = getRole(ROLE_FOURNISSEUR.name());
        fournisseur.setRoles(List.of(role));
        return fournisseurRepository.save(fournisseur);

    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(String roleName) {
        if(roleRepository.existsByNomRole(roleName))
            throw  new EntityAlreadyExistsException("Role "+ roleName +"already exists");

        Optional<Role> role = Optional.ofNullable(Role.builder()
                .nomRole(roleName)
                .build());

        return roleRepository.save(role.get());
    }

    @Override
    public Role getRole(String roleName) {
        return roleRepository.findRoleByNomRole(roleName)
                .orElseThrow(() -> new NotFoundException("role: " + roleName + " NOT FOUND !!"));

    }

    @Override
    public void deleteUser(String userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            if (hasRole(request.getUsername(), ROLE_FOURNISSEUR) && isAccountBlocked(request.getUsername())) {
                return null;
            } else {
                UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
                User user = getUserByUserName(userDetails.getUsername());
                if (!user.isEnabled())
                    throw new InvalidCredentialsException("you need to verify your email first");
                String accessToken = jwtService.generateAccessToken(userDetails, user.getId());
                String refreshToken = jwtService.generateRefreshToken(userDetails);

                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                saveUserToken(user, refreshToken);

                return AuthResponse
                        .builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("WRONG CREDENTIALS");
        }
    }

    public boolean hasRole(String userName, RoleName nomRole) {
        User user = getUserByUserName(userName);
        Collection<Role> roles = user.getRoles();
        return roles.stream()
                .map(Role::getNomRole)
                .anyMatch(nomRole::equals);
    }

    public AuthResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String refreshToken = authHeader.substring(7);
            String userName = jwtService.extractUsername(refreshToken);
            UserDetails user = userDetailsService.loadUserByUsername(userName);
            String accessToken = jwtService.generateAccessToken(user, request.getRequestId());
            return AuthResponse.builder()
                    .refreshToken(refreshToken)
                    .accessToken(accessToken)
                    .build();
        }
        return null;
    }

    public boolean isAccountBlocked(String userName) {
        Fournisseur fournisseur = fournisseurRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("fournisseur " + userName + " not found"));
        if (fournisseur.isBlackList())
            throw new AccountBannedException("Account: Blocked" + "\n+" + " Motif de blockage: " + fournisseur.getMotifDeBlockage());

        return false;
    }

    public void revokeAllUserTokens(User user) {
        List<Token> validUserToken = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        Token savedAccessToken = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(savedAccessToken);
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token, TokenType.VERIFICATION_EMAIL_TOKEN);

        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken)
                .orElseThrow(() -> new NotFoundException("verification token " + verificationToken + "not found"));
    }


}



