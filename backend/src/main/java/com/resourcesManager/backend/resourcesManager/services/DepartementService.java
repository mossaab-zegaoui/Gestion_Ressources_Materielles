package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.Departement;

import java.util.List;

public interface DepartementService {
    List<Departement> getAllDepartements();

    Departement getDepartementById(Long id);


    Departement saveDepartement(String nomDepartement);

    Departement updateDepartement(Departement departement);

    void deleteDepartement(Long id);
}
