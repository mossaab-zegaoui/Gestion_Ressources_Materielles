package com.resourcesManager.backend.resourcesManager.services.impl;


import com.resourcesManager.backend.resourcesManager.model.NotifFournisseur;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.NotifFournisseurRepository;
import com.resourcesManager.backend.resourcesManager.services.NotifFournisseurService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor

public class NotifFournisseurServiceImpl implements NotifFournisseurService {

    private final NotifFournisseurRepository notifFournisseurRepository;


    @Override
    public List<NotifFournisseur> getNotifsByIdFournisseurNotSeen(String id) {
        return notifFournisseurRepository.getNotifFournisseurByIdFournisseurAndIsSeenIsFalse(id);
    }

    @Override
    public List<NotifFournisseur> getAllNotifications() {
        return notifFournisseurRepository.findAllByAndIsSeenIsFalse();
    }

    @Override
    public void addNotifFournisseur(NotifFournisseur notifFournisseur) {
        notifFournisseurRepository.save(notifFournisseur);
    }

    @Override
    public void notifSeen(Long id) {
        NotifFournisseur notifFournisseur = notifFournisseurRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("notification with id:" + id + " not found"));
        notifFournisseur.setIsSeen(true);
        notifFournisseurRepository.save(notifFournisseur);
    }
}
