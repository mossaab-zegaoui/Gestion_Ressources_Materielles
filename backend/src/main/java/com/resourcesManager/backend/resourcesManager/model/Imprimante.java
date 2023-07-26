package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@DiscriminatorValue("Imprimante")
public class Imprimante extends Ressource {

    private String resolution;
    private String vitesseImpression;
}
