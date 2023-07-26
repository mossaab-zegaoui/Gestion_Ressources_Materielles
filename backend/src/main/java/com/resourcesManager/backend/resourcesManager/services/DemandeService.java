package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.Demande;

import java.util.List;

public interface DemandeService {

    void createDemande(Long idDepartement);


    Demande demandeSeen(Long id);

    List<Demande> getAllDemandesByMembreId(String id);

    List<Demande> getAllDemandesByIdDepartement(Long id);

    List<Demande> getAllDemandes();

    void deleteDemande(Long id);

}
