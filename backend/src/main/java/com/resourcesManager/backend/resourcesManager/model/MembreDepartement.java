package com.resourcesManager.backend.resourcesManager.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Entity
@Builder @AllArgsConstructor @NoArgsConstructor @Data
public class MembreDepartement  extends User {

    private String domaineExpertise;
    private String laboratoire;
    @ManyToOne
    private Departement departement;

}
