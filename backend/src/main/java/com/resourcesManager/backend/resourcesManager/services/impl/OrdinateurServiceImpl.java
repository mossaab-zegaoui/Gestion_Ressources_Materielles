package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Ordinateur;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.OrdinateurRepository;
import com.resourcesManager.backend.resourcesManager.services.OrdinateurService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class OrdinateurServiceImpl implements OrdinateurService {

    private final OrdinateurRepository ordinateurRepository;


    @Override
    public Ordinateur addOrdinateur(Ordinateur ordinateur) {
        return ordinateurRepository.save(ordinateur);
    }

    @Override
    public List<Ordinateur> getAllOrdinateurs() {
        List<Ordinateur> ordinateurs = ordinateurRepository.findAll();
        return filterOrdinateurs(ordinateurs);
    }

    @Override
    public List<Ordinateur> getOrdinateursByMembreDepartement(String id) {
        List<Ordinateur> ordinateurs = ordinateurRepository.getOrdinateurByIdMembreDepartement(id);
        return filterOrdinateurs(ordinateurs);
    }

    @Override
    public List<Ordinateur> getOrdinateursByDepartement(Long id) {
        List<Ordinateur> ordinateurs = ordinateurRepository.getOrdinateurByIdDepartement(id);
        return filterOrdinateurs(ordinateurs);
    }

    @Override
    public List<Ordinateur> getOrdinateursByFournisseur(String id) {
        List<Ordinateur> ordinateurs = ordinateurRepository.getOrdinateurByIdFournisseur(id);
        return filterOrdinateurs(ordinateurs);
    }

    @Override
    public Ordinateur getOrdinateur(Long id) {
        Ordinateur ordinateur = ordinateurRepository.findById(id).orElseThrow(() ->
                new NotFoundException("L'ordinateur avec l'id = " + id + " est introuvable")
        );
        if (ordinateur.getIsDeleted())
            throw new NotFoundException("L'ordinateur avec l'id = " + id + " est supprimé");

        return ordinateur;
    }

    @Override
    public Ordinateur updateOrdinateur(Ordinateur ordinateur) {
        return ordinateurRepository.save(ordinateur);
    }

    @Override
    public void deleteOrdinateur(Long id) {
        Ordinateur ordinateur = this.getOrdinateur(id);
        ordinateur.setIsDeleted(true);
        ordinateurRepository.save(ordinateur);
    }

    public List<Ordinateur> filterOrdinateurs(List<Ordinateur> ordinateurs) {
        List<Ordinateur> filteredOrdinateurs = ordinateurs.stream()
                .filter(ordinateur -> "Ordinateur".equals(ordinateur.getType()))
                .filter(ordinateur -> !ordinateur.getIsDeleted())
                .toList();
        return filteredOrdinateurs;


    }

    @Override
    public List<Ordinateur> getOrdinateurLivrees() {
        List<Ordinateur> ordinateurNonLivre = filterOrdinateurs(ordinateurRepository.findAllByCodeBarreIsNullAndMarqueIsNotNull());
        return ordinateurNonLivre;
    }

    @Override
    public List<Ordinateur> getOrdinateurDisponibles() {
        List<Ordinateur> ordinateurDisponible = filterOrdinateurs(ordinateurRepository.findAllByCodeBarreIsNotNull());
        return ordinateurDisponible;
    }

}
