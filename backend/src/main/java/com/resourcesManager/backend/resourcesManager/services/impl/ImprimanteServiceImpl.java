package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Imprimante;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.ImprimanteRepository;
import com.resourcesManager.backend.resourcesManager.services.ImprimanteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class ImprimanteServiceImpl implements ImprimanteService {

    private final ImprimanteRepository imprimanteRepository;

    @Override
    public Imprimante saveImprimante(Imprimante imprimante) {
        return imprimanteRepository.save(imprimante);
    }

    @Override
    public List<Imprimante> getAllImprimantes() {

        List<Imprimante> imprimantesDisponibles = imprimanteRepository.findAll()
                .stream()
                .filter(imprimante -> !imprimante.getIsDeleted())
                .collect(Collectors.toList());

        return imprimantesDisponibles;
    }

    @Override
    public List<Imprimante> getImprimantesByMembreDepartement(String id) {
        List<Imprimante> imprimantesDisponibles = imprimanteRepository.getImprimanteByIdMembreDepartement(id)
                .stream()
                .filter(imprimante -> !imprimante.getIsDeleted())
                .collect(Collectors.toList());

        return imprimantesDisponibles;
    }

    @Override
    public List<Imprimante> getImprimantesByDepartement(Long id) {
        List<Imprimante> imprimantesDisponibles = imprimanteRepository.getImprimanteByIdDepartement(id)
                .stream()
                .filter(imprimante -> !imprimante.getIsDeleted())
                .collect(Collectors.toList());

        return imprimantesDisponibles;
    }

    @Override
    public List<Imprimante> getImprimantesByFournisseur(String id) {
        List<Imprimante> imprimantesDisponibles = imprimanteRepository.getImprimanteByIdFournisseur(id)
                .stream()
                .filter(imprimante -> !imprimante.getIsDeleted())
                .collect(Collectors.toList());

        return imprimantesDisponibles;
    }

    @Override
    public Imprimante getImprimante(Long id) {
        Optional<Imprimante> imprimante = imprimanteRepository.findById(id);
        if (imprimante.isEmpty()) throw new NotFoundException("L'imprimante with id : " + id + " est introuvable");
        if (imprimante.get().getIsDeleted())
            throw new NotFoundException("L'imprimante avec l'id = " + id + " est supprime");

        return imprimante.get();

    }

    @Override
    public Imprimante updateImprimante(Imprimante imprimante) {
        return imprimanteRepository.save(imprimante);
    }

    @Override
    public void deleteImprimante(Long id) {
        Imprimante imprimante = this.getImprimante(id);
        imprimante.setIsDeleted(true);
        imprimanteRepository.save(imprimante);
    }

    public List<Imprimante> filterImprimantes(List<Imprimante> imprimantes) {
        List<Imprimante> imprimanteList = imprimantes.stream()
                .filter(imprimante -> "Imprimante".equals(imprimante.getType()))
                .filter(imprimante -> !imprimante.getIsDeleted())
                .toList();

        return imprimanteList;
    }

    @Override
    public List<Imprimante> getImprimantesLivrees() {
        List<Imprimante> imprimantesLivrees = imprimanteRepository.findAllByCodeBarreIsNullAndMarqueIsNotNull()
                .stream()
                .filter(imprimante -> !imprimante.getIsDeleted())
                .collect(Collectors.toList());

        return imprimantesLivrees;

    }

    @Override
    public List<Imprimante> getImprimantesDisponibles() {

        List<Imprimante> imprimanteDisponible = imprimanteRepository.findAllByCodeBarreIsNotNull()
                .stream()
                .filter(imprimante -> !imprimante.getIsDeleted())
                .collect(Collectors.toList());

        return imprimanteDisponible;
    }

}
