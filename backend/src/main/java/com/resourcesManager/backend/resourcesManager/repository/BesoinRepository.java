package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.Besoin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BesoinRepository extends JpaRepository<Besoin, Long> {

    List<Besoin> findBesoinByIdMembreDepartementAndIsAffectedIsFalse(String id);
    List<Besoin> findBesoinByIdDepartement(Long id);
    List<Besoin> findBesoinByIdMembreDepartement(String id);
    Optional<Besoin> findBesoinByIdMembreDepartementAndIsBesoinInAppelOffreIsFalse(String id);
    List<Besoin> findBesoinByIdDepartementAndIsBesoinInAppelOffreIsFalse(Long id);
    List<Besoin> findAllByIsBesoinInAppelOffreIsFalse();

}
