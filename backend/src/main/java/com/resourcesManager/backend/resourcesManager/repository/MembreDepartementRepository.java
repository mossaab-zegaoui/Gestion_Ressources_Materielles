package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.MembreDepartement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MembreDepartementRepository extends JpaRepository<MembreDepartement, String> {

    List<MembreDepartement> getMembreDepartementByDepartementId(Long idDepartement);
    Optional<MembreDepartement> findByUsername(String username);

}
