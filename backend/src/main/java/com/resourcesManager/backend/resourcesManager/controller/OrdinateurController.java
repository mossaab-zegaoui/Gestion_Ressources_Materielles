package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Ordinateur;
import com.resourcesManager.backend.resourcesManager.services.OrdinateurService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordinateurs")
@CrossOrigin("*")
public class OrdinateurController {

    private final OrdinateurService ordinateurService;

    public OrdinateurController(OrdinateurService ordinateurService) {
        this.ordinateurService = ordinateurService;
    }

    @GetMapping()
    List<Ordinateur> getAllOrdinateurs() {
        return ordinateurService.getAllOrdinateurs();
    }

    @GetMapping("/membreDepartement/{id}")
    List<Ordinateur> getOrdinateursByMembreDepartement(@PathVariable String id) {
        return ordinateurService.getOrdinateursByMembreDepartement(id);
    }

    @GetMapping("/departement/{id}")
    List<Ordinateur> getOrdinateursByDepartement(@PathVariable Long id) {
        return ordinateurService.getOrdinateursByDepartement(id);
    }

    @GetMapping("/fournisseur/{id}")
    List<Ordinateur> getOrdinateursByFournisseur(@PathVariable String id) {
        return ordinateurService.getOrdinateursByFournisseur(id);
    }

    @GetMapping("/{id}")
    Ordinateur getOrdinateur(@PathVariable Long id) {
        return ordinateurService.getOrdinateur(id);
    }

    @PutMapping("/{id}")
    Ordinateur updateOrdinateur(@PathVariable Long id, @RequestBody Ordinateur ordinateur) {
        ordinateur.setId(id);
        return ordinateurService.updateOrdinateur(ordinateur);
    }

    @DeleteMapping("/{id}")
    void deleteOrdinateur(@PathVariable Long id) {
        ordinateurService.deleteOrdinateur(id);
    }

    @GetMapping("/listeDisponible")
    List<Ordinateur> getOrdinateursDisponibles(){
        return ordinateurService.getOrdinateurDisponibles();
    }

    @GetMapping("/listeLivree")
    List<Ordinateur> getOrdinateursLivrees(){
        return ordinateurService.getOrdinateurLivrees();
    }

}
