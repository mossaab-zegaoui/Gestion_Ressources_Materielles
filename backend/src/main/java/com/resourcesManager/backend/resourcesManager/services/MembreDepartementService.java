package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.MembreDepartement;

import java.util.List;

 public interface MembreDepartementService {

     List<MembreDepartement> getAllMembresDepartement();

      MembreDepartement getMembreDepartement(String id);

    MembreDepartement saveMembre(MembreDepartement enseignant);


     MembreDepartement updateMembreDepartement(MembreDepartement membreDepartement);

     List<MembreDepartement> getMembresByIdDepartement(Long idDepartement);

     void deleteMembreDepartement(String id);

}
