package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.*;
import com.resourcesManager.backend.resourcesManager.enums.RoleName;
import com.resourcesManager.backend.resourcesManager.form.AuthResponse;
import com.resourcesManager.backend.resourcesManager.model.token.Token;
import com.resourcesManager.backend.resourcesManager.exceptions.AccountBannedException;
import com.resourcesManager.backend.resourcesManager.exceptions.EntityAlreadyExistsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.*;
import com.resourcesManager.backend.resourcesManager.services.impl.AuthenticationServiceImpl;
import com.resourcesManager.backend.resourcesManager.services.impl.CustomUserDetailsService;
import com.resourcesManager.backend.resourcesManager.services.impl.JwtService;
import com.resourcesManager.backend.resourcesManager.services.impl.PanneServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private FournisseurRepository fournisseurRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private PanneRepository panneRepository;
    @Mock
    private RessourceRepository ressourceRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private JwtService jwtService;
    @Mock
    private CustomUserDetailsService customUserDetailsService;
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @InjectMocks
    private PanneServiceImpl panneService;
    private User USER_AMINE;
    private User USER_KHALID;
    private Role ROLE_ADMIN;
    private Role ROLE_USER;
    private Role ROLE_RESPONSABLE;
    private Panne PANNE_1;
    private Panne PANNE_2;
    private Ressource ORDINATEUR;
    private Ressource IMPRIMANTE;

    @BeforeEach
    void setupService() {
        USER_AMINE = new User();
        USER_AMINE.setUsername("amine");
        USER_AMINE.setUsername("amine_123");
        USER_AMINE.setEmail("amine@gmail.com");
        USER_AMINE.setPassword("amine_password");
        USER_KHALID = new User();
        USER_KHALID.setUsername("khalid");
        USER_KHALID.setUsername("khalid_123");
        USER_KHALID.setEmail("khalid@gmail.com");
        USER_KHALID.setPassword("khalid_password");
//
        ROLE_ADMIN = new Role();
        ROLE_ADMIN.setId(1L);
        ROLE_ADMIN.setNomRole("ADMIN");
        ROLE_USER = new Role();
        ROLE_USER.setId(2L);
        ROLE_USER.setNomRole("USER");
        ROLE_RESPONSABLE = new Role();
        ROLE_RESPONSABLE.setNomRole("responsable");
//
        PANNE_1 = new Panne();
        PANNE_1.setId(1L);
        PANNE_1.setIsTreated(false);
        PANNE_1.setIdRessource(1L);
        PANNE_1.setDateApparition(Date.valueOf(LocalDate.now()));

        PANNE_2 = new Panne();
        PANNE_2.setId(2L);
        PANNE_2.setIsTreated(false);
        PANNE_2.setIdRessource(2L);
        PANNE_2.setDateApparition(Date.valueOf(LocalDate.now()));

        ORDINATEUR = new Ressource();
        ORDINATEUR.setId(1L);
        ORDINATEUR.setIdMembreDepartement("1");
        ORDINATEUR.setCodeBarre(UUID.randomUUID().toString());
        ORDINATEUR.setDateLivraison(Date.valueOf(LocalDate.of(2023, 06, 11)));
        ORDINATEUR.setDateFinGarantie(Date.valueOf(LocalDate.of(2023, 07, 11)));
        ORDINATEUR.setType("Ordinateur");
        ORDINATEUR.setMarque("Canon");
        ORDINATEUR.setPrix(25);
        IMPRIMANTE = new Ressource();
        IMPRIMANTE.setId(2L);
        IMPRIMANTE.setIdMembreDepartement("2");
        IMPRIMANTE.setCodeBarre(UUID.randomUUID().toString());
        IMPRIMANTE.setDateLivraison(Date.valueOf(LocalDate.of(2023, 06, 11)));
        IMPRIMANTE.setDateFinGarantie(Date.valueOf(LocalDate.of(2023, 07, 11)));
        IMPRIMANTE.setType("Imprimante");
        IMPRIMANTE.setMarque("HP");
        IMPRIMANTE.setPrix(199);


    }

    @Test
    void getAllUsers_Should_Return_ListofAllUsers() {
//
        List<User> expectedUsers = List.of(USER_AMINE, USER_KHALID);
        when(userRepository.findAll()).thenReturn(expectedUsers);
//
        List<User> users = authenticationService.getAllUsers();
//
        Assertions.assertThat(users).isNotEmpty();
        assertEquals(expectedUsers.size(), users.size());
        assertEquals(expectedUsers, users);
    }

    @Test
    void getAllRoles_Should_Return_ListofAllRoles() {
//
        List<Role> expectedRoles = List.of(ROLE_ADMIN, ROLE_USER);
        when(roleRepository.findAll()).thenReturn(expectedRoles);
//
        List<Role> roles = authenticationService.getAllRoles();
//
        Assertions.assertThat(roles).isNotEmpty();
        assertEquals(expectedRoles.size(), roles.size());
        assertEquals(expectedRoles, roles);
    }

    @Test
    void getUserByUserName_ShouldReturnUser() {
        when(userRepository.findUserByUsername(USER_AMINE.getUsername())).thenReturn(Optional.ofNullable(USER_AMINE));
        User user = authenticationService.getUserByUserName(USER_AMINE.getUsername());
        assertNotNull(user);
        assertEquals(USER_AMINE, user);
    }

    @Test
    void getUserByUserName_ShouldThrowNotFoundException() {
        when(userRepository.findUserByUsername(USER_AMINE.getUsername())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            authenticationService.getUserByUserName(USER_AMINE.getUsername());
        });
        String actualMessage = exception.getMessage();
        String expectedMessage = "user " + USER_AMINE.getUsername() + " NOT FOUND !!! ";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getUserById_ShouldReturnUser() {
        when(userRepository.findById(USER_AMINE.getId())).thenReturn(Optional.ofNullable(USER_AMINE));
        User user = authenticationService.getUserById(USER_AMINE.getId());
        assertNotNull(user);
        assertEquals(USER_AMINE, user);
    }

    @Test
    void getUserById_ShouldThrowNotFoundException() {
        when(userRepository.findById(USER_AMINE.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                authenticationService.getUserById(USER_AMINE.getId()));
        String actualMessage = exception.getMessage();
        String expectedMessage = "user with id: " + USER_AMINE.getId() + " NOT FOUND !!! ";
        assertNotNull(expectedMessage);
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void saveUser_ShouldReturnSavedUser() {
//
        String encodedPassword = "$2a$04$4Jt/RqPoiCEHqmN78S60s.gGtHVJtabZkBcnFAbY4Y6hXD8jhcqcS";
        User expectedUser = USER_AMINE;
        expectedUser.setPassword(encodedPassword);
        expectedUser.setRoles(List.of(ROLE_USER));
        when(userRepository.findUserByUsername(USER_AMINE.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(USER_AMINE.getPassword())).thenReturn(encodedPassword);
        when(roleRepository.findRoleByNomRole("USER")).thenReturn(Optional.ofNullable(ROLE_USER));
        when(userRepository.save(USER_AMINE)).thenReturn(expectedUser);
//
        User savedUser = authenticationService.saveUser(USER_AMINE);
//
        assertNotNull(savedUser);
        assertEquals(expectedUser, savedUser);
    }


    @Test
    void saveUser_ShouldThrow_EntityAlreadyExistsException() {
        when(userRepository.findUserByUsername(USER_AMINE.getUsername())).thenReturn(Optional.ofNullable(USER_AMINE));
        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () -> {
            authenticationService.saveUser(USER_AMINE);
        });
        String actualMessage = exception.getMessage();
        String expectedMessage ="Usename:" + USER_AMINE.getUsername() + " already exists";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveResponsable_shouldReturnsavedResponsable() {
//
        String encodedPassword = "$2a$04$4Jt/RqPoiCEHqmN78S60s.gGtHVJtabZkBcnFAbY4Y6hXD8jhcqcS";
        User expectedUser = USER_AMINE;
        expectedUser.setRoles(List.of(ROLE_RESPONSABLE));
        expectedUser.setPassword(encodedPassword);
        when(userRepository.findUserByUsername(USER_AMINE.getUsername())).thenReturn(Optional.empty());
        when(roleRepository.findRoleByNomRole("responsable")).thenReturn(Optional.ofNullable(ROLE_RESPONSABLE));
        when(userRepository.save(USER_AMINE)).thenReturn(expectedUser);
//
        User savedUser = authenticationService.saveResponsable(USER_AMINE);
//
        assertNotNull(savedUser);
        assertEquals(expectedUser, savedUser);
    }

    @Test
    void saveResponsable_ShouldThrowEntityAlreadyExistsException() {
        when(userRepository.findUserByUsername(USER_AMINE.getUsername())).thenReturn(Optional.of(USER_AMINE));

        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class, () ->
                authenticationService.saveResponsable(USER_AMINE));
        String expectedMessage = "Usename:" + USER_AMINE.getUsername() + " already exists";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveRole_shouldReturnSavedNewRole() {
        String nomRole = "USER";
        Role newRole = Role.builder()
                .nomRole(nomRole)
                .build();
        when(roleRepository.findRoleByNomRole(nomRole)).thenReturn(Optional.empty());
        when(roleRepository.save(newRole)).thenReturn(newRole);
//
        Role role = authenticationService.saveRole(nomRole);
        assertEquals(role, newRole);
    }

    @Test
    void saveRole_shouldReturnSavedRole() {
        String nomRole = "USER";
        Role newRole = Role.builder()
                .nomRole(nomRole)
                .build();
        when(roleRepository.findRoleByNomRole(nomRole)).thenReturn(Optional.ofNullable(newRole));
        when(roleRepository.save(newRole)).thenReturn(newRole);
//
        Role role = authenticationService.saveRole(nomRole);
        assertEquals(role, newRole);
    }

    @Test
    void getRole_shouldReturnRole() {
        when(roleRepository.findRoleByNomRole(ROLE_USER.getNomRole())).thenReturn(Optional.ofNullable(ROLE_USER));
        Role role = authenticationService.getRole(ROLE_USER.getNomRole());
        assertEquals(ROLE_USER, role);
    }

    @Test
    void getRole_shouldThrowNotFoundException() {
        String roleName = "USER";
        when(roleRepository.findRoleByNomRole(roleName)).thenReturn(Optional.empty());
//
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                authenticationService.getRole(roleName));
        String actualMessage = exception.getMessage();
        String expectedMessage = "role: " + roleName + " NOT FOUND !!";
//
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void deleteUser_ShouldDeleteUser() {
        when(userRepository.findById(USER_AMINE.getId())).thenReturn(Optional.ofNullable(USER_AMINE));

        authenticationService.deleteUser(USER_AMINE.getId());
        verify(userRepository, times(1)).findById(USER_AMINE.getId());
        verify(userRepository, times(1)).delete(USER_AMINE);

    }

    @Test
    public void hasRole_souldReturnTrue() {
        String userName = "khalid123";
        User expectedUser = new User();
        expectedUser.setUsername(userName);
        expectedUser.setRoles(List.of(ROLE_USER, ROLE_ADMIN));
        when(userRepository.findUserByUsername(userName)).thenReturn(Optional.of(expectedUser));
        boolean result = authenticationService.hasRole(userName, RoleName.valueOf(ROLE_USER.getNomRole()));
        assertTrue(result);
    }

    @Test
    public void hasRole_shouldReturnFalse() {
        String userName = "khalid123";
        User expectedUser = new User();
        expectedUser.setUsername(userName);
        expectedUser.setRoles(List.of(ROLE_ADMIN));
        when(userRepository.findUserByUsername(userName)).thenReturn(Optional.of(expectedUser));
        boolean result = authenticationService.hasRole(userName, RoleName.valueOf(ROLE_USER.getNomRole()));
        assertFalse(result);
    }

    @Test
    public void isAccountBlocked_shouldReturnFalse() {
        String userName = "tariq12";
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setUsername(userName);
        fournisseur.setNomSociete("CGI");
        fournisseur.setBlackList(false);
        when(fournisseurRepository.findByUsername(userName)).thenReturn(Optional.of(fournisseur));
        boolean result = authenticationService.isAccountBlocked(userName);
        assertFalse(result);
    }

    @Test
    public void isAccountBlocked_shouldThrowAccountBlockedException() {
        String userName = "tariq12";
        Fournisseur fournisseur = new Fournisseur();
        fournisseur.setUsername(userName);
        fournisseur.setNomSociete("CGI");
        fournisseur.setBlackList(true);
        fournisseur.setMotifDeBlockage("posting late");
//
        when(fournisseurRepository.findByUsername(userName)).thenReturn(Optional.of(fournisseur));
        AccountBannedException exception = assertThrows(AccountBannedException.class, () -> {
            authenticationService.isAccountBlocked(userName);
        });
//
        String actualMessage = exception.getMessage();
        String expectedMessage = "Account: Blocked, Motif de blockage: " + fournisseur.getMotifDeBlockage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getNotTreatedPanne_should_Return_ListofPannesAvecRessourcesNotTreated() {
//

        List<Panne> pannes = List.of(PANNE_1, PANNE_2);
        List<Ressource> ressources = List.of(ORDINATEUR, IMPRIMANTE);

        when(panneRepository.findAllByIsTreatedFalse()).thenReturn(pannes);
        when(ressourceRepository.findAll()).thenReturn(ressources);
        PanneAvecRessource panneAvecRessource_1 = PanneAvecRessource.builder()
                .idPanne(PANNE_1.getId())
                .idRessource(ORDINATEUR.getId())
                .idMembreDepartement(ORDINATEUR.getIdMembreDepartement())
                .type(ORDINATEUR.getType())
                .marque(ORDINATEUR.getMarque())
                .dateApparition(PANNE_1.getDateApparition())
                .build();
        PanneAvecRessource panneAvecRessource_2 = PanneAvecRessource.builder()
                .idPanne(PANNE_2.getId())
                .idRessource(IMPRIMANTE.getId())
                .idMembreDepartement(IMPRIMANTE.getIdMembreDepartement())
                .type(IMPRIMANTE.getType())
                .marque(IMPRIMANTE.getMarque())
                .dateApparition(PANNE_2.getDateApparition())
                .build();
        List<PanneAvecRessource> panneAvecRessources = List.of(panneAvecRessource_1, panneAvecRessource_2);
//
        List<PanneAvecRessource> result = panneService.getNotTreatedPanne();
//
        Assertions.assertThat(result).isNotEmpty();
        assertEquals(panneAvecRessources.size(), result.size());
        assertEquals(panneAvecRessources, result);
    }
    @Test
    void refreshToken() {
//
        String authHeader = "Bearer refreshToken";
        String userName = "testUser";
        String accessToken = "accessToken";
        String refreshToken = authHeader.substring(7);
        User user = new User();

        user.setUsername(userName);
        user.setEmail("mossaab@gmail.com");
        user.setPassword("password_123");
        AuthResponse expectedResponse = AuthResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
//
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);
        when(request.getHeader("Authorization")).thenReturn(authHeader);
        when(jwtService.extractUsername(refreshToken)).thenReturn(userName);
        when(customUserDetailsService.loadUserByUsername(userName)).thenReturn(userDetails);
        when(jwtService.generateAccessToken(userDetails, request.getRequestId())).thenReturn(accessToken);

        AuthResponse result = authenticationService.refreshToken(request);
//
        verify(jwtService, times(1)).generateAccessToken(userDetails, user.getId());
        assertNotNull(result);
        assertEquals("refreshToken", result.getRefreshToken());

    }

    @Test
    void revokeAllUserTokens() {
        User user = new User();
        user.setUsername("enseignant");
        user.setPassword("password_123");

        Token token1 = new Token();
        token1.setUser(user);
        token1.setToken("token1");
        token1.setExpired(false);
        token1.setRevoked(false);
        Token token2 = new Token();
        token2.setUser(user);
        token2.setToken("token2");
        token2.setExpired(false);
        token2.setRevoked(false);
        List<Token> tokens = Arrays.asList(token1, token2);

        when(tokenRepository.findAllValidTokensByUser(user.getId())).thenReturn(tokens);
        when(tokenRepository.saveAll(tokens)).thenReturn(tokens);

        // When
        authenticationService.revokeAllUserTokens(user);

        // Then
        verify(tokenRepository, times(1)).findAllValidTokensByUser(user.getId());
        verify(tokenRepository, times(1)).saveAll(tokens);

        assertTrue(token1.isExpired());
        token1.isRevoked();
        token2.isExpired();
        assertTrue(token2.isRevoked());
    }
}
