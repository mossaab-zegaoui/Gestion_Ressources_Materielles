package com.resourcesManager.backend.resourcesManager.services;


import com.resourcesManager.backend.resourcesManager.model.Ordinateur;

import java.util.List;

public interface OrdinateurService {

    Ordinateur addOrdinateur(Ordinateur ordinateur);
    List<Ordinateur> getAllOrdinateurs();
    List<Ordinateur> getOrdinateursByMembreDepartement(String id);
    List<Ordinateur> getOrdinateursByDepartement(Long id);
    List<Ordinateur> getOrdinateursByFournisseur(String id);
    Ordinateur getOrdinateur(Long id);
    Ordinateur updateOrdinateur(Ordinateur ordinateur);

    void deleteOrdinateur(Long id);
    List<Ordinateur> getOrdinateurLivrees();
    List<Ordinateur> getOrdinateurDisponibles();

}
