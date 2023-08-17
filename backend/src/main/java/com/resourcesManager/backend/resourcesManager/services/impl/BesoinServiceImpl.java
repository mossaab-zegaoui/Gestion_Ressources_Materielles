package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.*;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.BesoinRepository;
import com.resourcesManager.backend.resourcesManager.repository.ImprimanteRepository;
import com.resourcesManager.backend.resourcesManager.repository.MembreDepartementRepository;
import com.resourcesManager.backend.resourcesManager.repository.OrdinateurRepository;
import com.resourcesManager.backend.resourcesManager.services.BesoinService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BesoinServiceImpl implements BesoinService {

    private final BesoinRepository besoinRepository;
    private final OrdinateurRepository ordinateurRepository;
    private final ImprimanteRepository imprimanteRepository;
    private final MembreDepartementRepository membreDepartementRepository;

    @Override
    public Besoin saveBesoin(Besoin besoin, String userId) {

        MembreDepartement membre = membreDepartementRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user id not found"));
        besoin.setIdMembreDepartement(userId);
        besoin.setIdDepartement(membre.getDepartement().getId());
        besoin.setDateDemande(new Date());
        return besoinRepository.save(besoin);
    }

    @Override
    public List<Besoin> getAllBesoins() {
        return besoinRepository.findAll();
    }

    @Override
    public List<Besoin> getBesoinsByDepartement(String id) {
        MembreDepartement membre = membreDepartementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("membre departement with  " + id + "not found"));

        return besoinRepository.findBesoinByIdDepartement(membre.getDepartement().getId());
    }

    @Override
    public List<Besoin> getBesoinsByMembreDepartement(String id) {
        return besoinRepository.findBesoinByIdMembreDepartement(id);
    }

    @Override
    public List<Besoin> getBesoinsDepartementNotInAppelOffre(Long id) {
        return besoinRepository.findBesoinByIdDepartementAndIsBesoinInAppelOffreIsFalse(id);
    }

    @Override
    public Besoin updateBesoin(Besoin besoin) {
        return besoinRepository.save(besoin);
    }

    @Override
    public Besoin getBesoinsDisponiblesByIdMembre(String id) {
        Optional<Besoin> besoin = besoinRepository.findBesoinByIdMembreDepartementAndIsBesoinInAppelOffreIsFalse(id);
        if (besoin.isEmpty())
            throw new NotFoundException("pas de besoins disponibles pour le membre de département: " + id);
        return besoin.get();
    }

    @Override
    public void besoinAddedInAppelOffre(Long id) {
        Optional<Besoin> besoin = besoinRepository.findById(id);
        if (besoin.isEmpty()) throw new NotFoundException("besoin with id = " + id + " not found ");
        besoin.get().setBesoinInAppelOffre(true);
        besoinRepository.save(besoin.get());
    }

    @Override
    public List<Besoin> getBesoinsNotInAppelOffre() {
        return besoinRepository.findAllByIsBesoinInAppelOffreIsFalse();
    }

    @Override
    public void deleteBesoinOfMembre(String id) {
        List<Besoin> besoins = besoinRepository.findBesoinByIdMembreDepartementAndIsAffectedIsFalse(id);
        besoinRepository.deleteAll(besoins);
    }

    @Override
    public void deleteBesoin(Long id) {
        Optional<Besoin> besoin = besoinRepository.findById(id);
        if (besoin.isEmpty()) throw new NotFoundException("besoin with id = " + id + " not found ");
        besoinRepository.deleteById(id);
    }

}
