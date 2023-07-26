package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Departement;
import com.resourcesManager.backend.resourcesManager.exceptions.EntityAlreadyExistsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.DepartementRepository;
import com.resourcesManager.backend.resourcesManager.services.DepartementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class DepartementServiceImpl implements DepartementService {

    private final DepartementRepository departementRepository;

    @Override
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }


    @Override
    public Departement getDepartementById(Long id) {
        Optional<Departement> departement = departementRepository.findById(id);
        if (departement.isEmpty()) throw new NotFoundException("Departement with id" + id + " not found");
        return departement.get();
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
        Optional<Departement> departement = departementRepository.findById(id);
        if (departement.isEmpty()) throw new NotFoundException("Departement with id" + id + " not found");
        departementRepository.deleteById(id);
    }

}
