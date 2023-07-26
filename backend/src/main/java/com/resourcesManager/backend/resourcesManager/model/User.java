package com.resourcesManager.backend.resourcesManager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.resourcesManager.backend.resourcesManager.model.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank(message = "username field can not be blank")
    private String username;
    @NotBlank(message = "password field can not be blank")
    private String password;
    private String cin;
    private String nom;
    private String prenom;
    @NotBlank(message = "email field can not be blank")
    private String email;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Collection<Token> tokens = new ArrayList<>();
    private boolean isEnabled;

}
