package com.resourcesManager.backend.resourcesManager.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PanneAvecRessource {
    private Long idPanne;
    private Long idRessource;
    private String idMembreDepartement;
    private String codeBarre;
    private String type;
    private String marque;
    private Date dateApparition;
}
