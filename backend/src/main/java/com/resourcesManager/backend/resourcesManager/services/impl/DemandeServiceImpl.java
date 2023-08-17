package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Demande;
import com.resourcesManager.backend.resourcesManager.model.MembreDepartement;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.DemandeRepository;
import com.resourcesManager.backend.resourcesManager.repository.DepartementRepository;
import com.resourcesManager.backend.resourcesManager.repository.MembreDepartementRepository;
import com.resourcesManager.backend.resourcesManager.services.DemandeService;
import com.resourcesManager.backend.resourcesManager.services.MembreDepartementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DemandeServiceImpl implements DemandeService {

    private final DemandeRepository demandeRepository;
    private final MembreDepartementRepository membreDepartementRepository;
    private final MembreDepartementService membreDepartementService;

    @Override
    public void createDemande(String userId) {
        MembreDepartement chefDepartement = membreDepartementRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Membre departement not found"));
        Long idDepartement = chefDepartement.getDepartement().getId();

        List<MembreDepartement> membresDepartement = membreDepartementService.getMembresByIdDepartement(idDepartement);

        List<Demande> demandes = membresDepartement.stream()
                .filter(membreDepartement -> !membreDepartement.getId().equals(userId))
                .map(membre -> createDemandeObject(membre.getId(), idDepartement))
                .collect(Collectors.toList());

        demandeRepository.saveAll(demandes);
    }

    private Demande createDemandeObject(String membreId, Long idDepartement) {
        return Demande.builder()
                .message("Envoyez vos besoins")
                .dateDemande(Date.valueOf(LocalDate.now()))
                .idDepartement(idDepartement)
                .idMembreDepartement(membreId)
                .isSeen(false)
                .build();
    }


    @Override
    public Demande demandeSeen(Long id) {
        Demande demande = demandeRepository.findById(id).orElseThrow(() ->
                new NotFoundException("La demande avec l'id = " + id + " est introuvable")
        );
        demande.setIsSeen(true);
        return demandeRepository.save(demande);
    }

    @Override
    public List<Demande> getAllDemandesByMembreId(String id) {
        return demandeRepository.findAllByIdMembreDepartement(id);
    }

    @Override
    public List<Demande> getAllDemandesByIdDepartement(Long id) {
        return demandeRepository.findAllByIdDepartement(id);
    }

    @Override
    public List<Demande> getAllDemandes() {
        return demandeRepository.findAll();
    }

    @Override
    public void deleteDemande(Long id) {
        demandeRepository.deleteById(id);
    }
}
