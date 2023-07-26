package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.RessourceAvecFournisseur;
import com.resourcesManager.backend.resourcesManager.repository.ResourceFournisseurRepository;
import com.resourcesManager.backend.resourcesManager.model.Ressource;
import com.resourcesManager.backend.resourcesManager.repository.RessourceRepository;
import com.resourcesManager.backend.resourcesManager.services.RessourceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class RessourceServiceImpl implements RessourceService {

    private final RessourceRepository ressourceRepository;
    private  final ResourceFournisseurRepository resourceFournisseurRepository;

    @Override
    public List<Ressource> getAllRessources() {
        return ressourceRepository.findAll();
    }

    @Override
    public List<Ressource> getRessourcesByMembreDepartement(String id) {
        return ressourceRepository.getRessourceByIdMembreDepartement(id);
    }

    @Override
    public Ressource addRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @Override
    public RessourceAvecFournisseur addRessourceFournisseur(RessourceAvecFournisseur ressourceAvecFournisseur) {
        return resourceFournisseurRepository.save(ressourceAvecFournisseur);
    }
    @Override
    public List<Ressource> addMultipleRessources(List<Ressource> ressources) {
        return ressourceRepository.saveAll(ressources);
    }

    @Override
    public Ressource updateRessource(Ressource ressource) {
        return ressourceRepository.save(ressource);
    }

    @Override
    public void deleteRessource(Long id) {
        Ressource ressource = ressourceRepository.findById(id).orElseThrow();
        ressource.setIsDeleted(true);
        ressourceRepository.save(ressource);
    }

    @Override
    public List<Ressource> getRessourcesByDepartement(Long id) {
        return ressourceRepository.getRessourceByIdDepartement(id);
    }

    @Override
    public List<Ressource> listRessourcesLivrees() {
        return ressourceRepository.findAllByCodeBarreIsNull();
    }

    @Override
    public List<Ressource> listRessourcesDisponibles() {
        return ressourceRepository.findAllByCodeBarreIsNullAndMarqueIsNotNull();
    }

}
