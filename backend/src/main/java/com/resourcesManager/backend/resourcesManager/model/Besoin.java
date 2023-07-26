package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;


@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Besoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDemande;
    private Date dateAffectation;
    private Boolean isAffected = false;
    private String idMembreDepartement;
    private Long idDepartement;
    private Boolean isBesoinInAppelOffre = false;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Ordinateur> ordinateurs = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Imprimante> imprimantes = new ArrayList<>();

}
