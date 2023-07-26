package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Fournisseur;
import com.resourcesManager.backend.resourcesManager.services.FournisseurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fournisseurs")
@CrossOrigin("*")
public class FournisseurController {

    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @PostMapping("/blackList/{id}")
    public ResponseEntity<?> blackListFournisseur(@PathVariable String id, @RequestBody String motif) {
        System.out.println(id + "\n" + motif);
        fournisseurService.blackListFournisseur(id, motif);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/listeFournisseur")
    List<Fournisseur> getFournisseur() {
        return fournisseurService.getAllFournisseurs();
    }

    @PutMapping("/{id}")
    Fournisseur updateFournisseur(@PathVariable String id, @RequestBody Fournisseur fournisseur) {
        return fournisseurService.updateFournisseur(fournisseur);
    }

}
