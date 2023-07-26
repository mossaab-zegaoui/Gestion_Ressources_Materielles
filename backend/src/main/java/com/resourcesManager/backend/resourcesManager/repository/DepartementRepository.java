package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.Departement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

    Page<Departement> findAll(Pageable pageable);

    Optional<Departement> findByNomDepartement(String nomDepartement);
    boolean existsByNomDepartement(String nomDepatement);

}
