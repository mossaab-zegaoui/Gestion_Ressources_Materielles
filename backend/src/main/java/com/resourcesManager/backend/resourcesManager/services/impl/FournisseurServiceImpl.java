package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Fournisseur;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.FournisseurRepository;
import com.resourcesManager.backend.resourcesManager.services.FournisseurService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class FournisseurServiceImpl implements FournisseurService {

    private final FournisseurRepository fournisseurRepository;


    @Override
    public void blackListFournisseur(String id, String motif) {

        Optional<Fournisseur> fournisseur = fournisseurRepository.findById(id);
        if (fournisseur.isEmpty()) throw new NotFoundException("Fournisseur introuvable");

        fournisseur.get().setBlackList(true);
        fournisseur.get().setMotifDeBlockage(motif);
        fournisseurRepository.save(fournisseur.get());
    }

    @Override
    public Fournisseur updateFournisseur(Fournisseur fournisseur) {
        return fournisseurRepository.save(fournisseur);
    }

    @Override
    public List<Fournisseur> getAllFournisseurs() {
        return fournisseurRepository.findAll();
    }
}
