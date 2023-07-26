package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.Ressource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RessourceRepository extends JpaRepository<Ressource, Long> {

    List<Ressource> getRessourceByIdDepartement(Long id);

    List<Ressource> getRessourceByIdMembreDepartement(String id);

    List<Ressource> findAllByCodeBarreIsNullAndMarqueIsNotNull();

    List<Ressource> findAllByCodeBarreIsNull();

}
