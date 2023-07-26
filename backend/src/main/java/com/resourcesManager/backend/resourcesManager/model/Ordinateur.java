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
@DiscriminatorValue("Ordinateur")
public class Ordinateur extends Ressource {

    private String cpu;
    private Long ram;
    private String disqueDur;
    private String ecran;


}
