package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Departement;
import com.resourcesManager.backend.resourcesManager.exceptions.EntityAlreadyExistsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.model.MembreDepartement;
import com.resourcesManager.backend.resourcesManager.repository.DepartementRepository;
import com.resourcesManager.backend.resourcesManager.repository.MembreDepartementRepository;
import com.resourcesManager.backend.resourcesManager.services.DepartementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class DepartementServiceImpl implements DepartementService {

    private final DepartementRepository departementRepository;
    private final MembreDepartementRepository membreDepartementRepository;

    @Override
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }


    @Override
    public Departement getDepartementById(Long id) {
        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Departement with id" + id + " not found"));
        return departement;
    }

    @Override
    public Departement saveDepartement(String nomDepartement) {
        if (departementRepository.existsByNomDepartement(nomDepartement))
            throw new EntityAlreadyExistsException("nom departement: " + nomDepartement + " already exists");
        Departement departement = new Departement();
        departement.setNomDepartement(nomDepartement);
        return departementRepository.save(departement);
    }

    @Override
    public Departement updateDepartement(Departement departement) {
        return departementRepository.save(departement);
    }

    @Override
    public void deleteDepartement(Long id) {

        Departement departement = departementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Departement with id\t" + id + " not found"));

        membreDepartementRepository.getMembreDepartementByDepartementId(id)
                .forEach(membreDepartement -> {
                    membreDepartement.setDepartement(null);
                    membreDepartementRepository.save(membreDepartement);
                });

        departementRepository.delete(departement);
    }
}
