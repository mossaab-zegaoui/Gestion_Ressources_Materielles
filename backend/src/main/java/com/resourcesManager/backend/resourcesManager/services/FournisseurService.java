package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.Fournisseur;

import java.util.List;

public interface FournisseurService {

     void blackListFournisseur(String id, String motif);

     Fournisseur updateFournisseur(Fournisseur f);
     List<Fournisseur> getAllFournisseurs();

}
