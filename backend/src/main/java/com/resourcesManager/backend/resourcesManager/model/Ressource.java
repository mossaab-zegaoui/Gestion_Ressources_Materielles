package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Date;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ressource_type", discriminatorType = DiscriminatorType.STRING)
@OnDelete(action = OnDeleteAction.CASCADE)
public class Ressource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codeBarre;
    private Date dateLivraison;
    private Date dateFinGarantie;
    private String marque;
    private double prix;
    private String type;
    private String idFournisseur;
    private String idMembreDepartement;
    private Long idDepartement;
    private Boolean isDeleted = false;

}
