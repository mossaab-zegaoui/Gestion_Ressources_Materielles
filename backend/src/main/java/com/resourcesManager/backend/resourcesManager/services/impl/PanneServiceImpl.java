package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Panne;
import com.resourcesManager.backend.resourcesManager.model.PanneAvecRessource;
import com.resourcesManager.backend.resourcesManager.model.Ressource;
import com.resourcesManager.backend.resourcesManager.repository.PanneRepository;
import com.resourcesManager.backend.resourcesManager.repository.RessourceRepository;
import com.resourcesManager.backend.resourcesManager.services.PanneService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor

public class PanneServiceImpl implements PanneService {

    private final PanneRepository panneRepository;
    private final RessourceRepository ressourceRepository;

    @Override
    public Panne addPanne(Panne panne) {
        return panneRepository.save(panne);
    }

    @Override
    public Panne updatePanne(Panne panne) {
        return panneRepository.save(panne);
    }

    @Override
    public void deletePanne(Long id) {
        panneRepository.deleteById(id);
    }

    @Override
    public List<Panne> getAllPannes() {
        return panneRepository.findAll();
    }

    @Override
    public List<Panne> getPannesMembreDepartement(String id) {
        return panneRepository.findPanneByIdMembreDepartement(id);
    }

    @Override
    public List<Panne> getPanneWithConstatNotNullAndDemandeNull() {
        return panneRepository.findByConstatIsNotNullAndDemandeIsNull();
    }

    @Override
    public List<Panne> getPannesNotTreated() {
        return panneRepository.findPanneByIsTreatedFalse();
    }

    @Override
    public List<Panne> getPanneWithDemandeNotNull() {
        return panneRepository.findByDemandeIsNotNull();
    }

    @Override
    public List<PanneAvecRessource> getNotTreatedPanne() {
        List<Panne> pannes = panneRepository.findAllByIsTreatedFalse();
        List<Ressource> ressources = ressourceRepository.findAll();

        List<PanneAvecRessource> panneAvecRessourceList = pannes.stream()
                .map(panne -> {
                    Ressource ressource = ressources.stream()
                            .filter(r -> r.getId() == panne.getIdRessource())
                            .findFirst()
                            .orElse(null);
                    PanneAvecRessource panneAvecRessource = PanneAvecRessource.builder()
                            .idPanne(panne.getId())
                            .idRessource(ressource == null ? null : ressource.getId())
                            .idMembreDepartement(ressource == null ? null :ressource.getIdMembreDepartement())
                            .dateApparition(panne.getDateApparition())
                            .type(ressource == null ? null : ressource.getType())
                            .marque(ressource == null ? null : ressource.getMarque())
                            .build();
                    return panneAvecRessource;
                })
                .collect(Collectors.toList());
        return panneAvecRessourceList;
    }
}
