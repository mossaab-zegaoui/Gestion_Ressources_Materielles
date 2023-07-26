package com.resourcesManager.backend.resourcesManager.repository;

import com.resourcesManager.backend.resourcesManager.model.NotifFournisseur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotifFournisseurRepository extends JpaRepository<NotifFournisseur, Long> {

     List<NotifFournisseur> getNotifFournisseurByIdFournisseurAndIsSeenIsFalse(String idFournisseur);
    List<NotifFournisseur> findAllByAndIsSeenIsFalse();

}
