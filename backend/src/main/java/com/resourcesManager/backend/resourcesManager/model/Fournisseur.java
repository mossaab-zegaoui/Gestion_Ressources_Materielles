package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Builder @Data @AllArgsConstructor @NoArgsConstructor
public class Fournisseur extends User {
    private String addresse;
    private String gerant;
    @NotBlank(message = "nomSociete field can not be blank")
    private String nomSociete;
    private boolean isBlackList;
    private String motifDeBlockage;
    @OneToMany
    private Collection<Offre> offres;

}