package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Demande;
import com.resourcesManager.backend.resourcesManager.services.DemandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/demandes")
@CrossOrigin("*")
public class DemandeController {

    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @PostMapping("/departements/{idDepartement}")
    public void createDemande(@PathVariable Long idDepartement) {
        demandeService.createDemande(idDepartement);
    }

    @PutMapping("/seen/{id}")
    public ResponseEntity<Demande> demandeSeen(@PathVariable Long id) {
        return ResponseEntity.ok(demandeService.demandeSeen(id));
    }



    @GetMapping("/membres/{id}")
    public ResponseEntity<List<Demande>> getAllDemandesByMembreId(@PathVariable String id) {
        return ResponseEntity.ok(demandeService.getAllDemandesByMembreId(id));
    }

    @GetMapping("/departements/{id}")
    public ResponseEntity<List<Demande>> getAllDemandesByIdDepartement(@PathVariable Long id) {
        return ResponseEntity.ok(demandeService.getAllDemandesByIdDepartement(id));
    }

    @GetMapping("")
    public ResponseEntity<List<Demande>> getAllDemandes() {
        return ResponseEntity.ok(demandeService.getAllDemandes());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDemande(@PathVariable Long id) {
        demandeService.deleteDemande(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
