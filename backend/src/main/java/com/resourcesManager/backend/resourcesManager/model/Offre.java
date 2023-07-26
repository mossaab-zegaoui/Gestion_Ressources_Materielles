package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateDebut;
    private Date dateFin;
    private Boolean isAffected;
    private  Boolean isRejected;
    private Boolean isWaiting;
    private String idFournisseur;
    @OneToMany(cascade = CascadeType.PERSIST)
    private Collection<RessourceAvecFournisseur> ressources = new ArrayList<>();
    private Long idAppelOffre;

}