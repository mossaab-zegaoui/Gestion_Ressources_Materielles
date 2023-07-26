package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.Imprimante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImprimanteRepository extends JpaRepository<Imprimante, Long> {

     List<Imprimante> getImprimanteByIdMembreDepartement(String id);
     List<Imprimante> getImprimanteByIdDepartement(Long id);
     List<Imprimante> getImprimanteByIdFournisseur(String id);
     List<Imprimante> findAllByCodeBarreIsNullAndMarqueIsNotNull();
    List<Imprimante> findAllByCodeBarreIsNotNull();
    void deleteImprimanteById(Long id);
}
