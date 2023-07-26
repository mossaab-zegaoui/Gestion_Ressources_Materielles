package com.resourcesManager.backend.resourcesManager.controller;


import com.resourcesManager.backend.resourcesManager.model.NotifFournisseur;
import com.resourcesManager.backend.resourcesManager.services.NotifFournisseurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifFournisseur")
@CrossOrigin("*")
public class NotifFournisseurController {

    private final NotifFournisseurService notifFournisseurService;

    public NotifFournisseurController(NotifFournisseurService notifFournisseurService) {
        this.notifFournisseurService = notifFournisseurService;
    }

    @GetMapping("/fournisseur/{id}")
    public List<NotifFournisseur> getNotifsFournisseur(@PathVariable String id) {
        return notifFournisseurService.getNotifsByIdFournisseurNotSeen(id);
    }
    @GetMapping("/fournisseur")
    public List<NotifFournisseur> getAllNotifsFournisseur(){
        return notifFournisseurService.getAllNotifications();
    }

    @PutMapping("/{id}")
    public void setNotifFournisseurSeen(@PathVariable Long id) {
        notifFournisseurService.notifSeen(id);
    }


}
