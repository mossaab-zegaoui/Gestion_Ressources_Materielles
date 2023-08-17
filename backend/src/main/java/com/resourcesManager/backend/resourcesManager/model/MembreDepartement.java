package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class MembreDepartement  extends User {

    private String domaineExpertise;
    private String laboratoire;
    @ManyToOne
    private Departement departement;

}
