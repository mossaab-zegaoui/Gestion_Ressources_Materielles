package com.resourcesManager.backend.resourcesManager.repository;
import com.resourcesManager.backend.resourcesManager.model.Technicien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TechnicienRepository extends JpaRepository<Technicien, String> {
    Optional<Technicien> findByUsername(String userName);
}