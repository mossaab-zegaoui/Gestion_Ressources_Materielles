package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.Besoin;

import java.util.List;

public interface BesoinService {

    Besoin saveBesoin(Besoin besoin);
    List<Besoin> getAllBesoins();
    List<Besoin> getBesoinsByDepartement(Long id);
    List<Besoin> getBesoinsByMembreDepartement(String id);
    void deleteBesoinOfMembre(String id);
    void deleteBesoin(Long id);
    List<Besoin> getBesoinsDepartementNotInAppelOffre(Long id);
    Besoin updateBesoin(Besoin besoin);
    Besoin getBesoinsDisponiblesByIdMembre(String id);
    List<Besoin> getBesoinsNotInAppelOffre();
    void besoinAddedInAppelOffre(Long id);

}
