package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.NotifFournisseur;

import java.util.List;

public interface NotifFournisseurService {

    public List<NotifFournisseur> getNotifsByIdFournisseurNotSeen(String id);
    List<NotifFournisseur> getAllNotifications();

    public void addNotifFournisseur(NotifFournisseur notifFournisseur);

    public void notifSeen(Long id);

}
