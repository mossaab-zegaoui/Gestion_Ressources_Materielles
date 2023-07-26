package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.AppelOffre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppelOffreRepository extends JpaRepository<AppelOffre,Long> {
    Optional<AppelOffre> findAppelOffreById(Long id);
}