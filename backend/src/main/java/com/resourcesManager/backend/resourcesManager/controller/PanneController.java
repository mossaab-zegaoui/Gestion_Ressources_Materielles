package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Panne;
import com.resourcesManager.backend.resourcesManager.model.PanneAvecRessource;
import com.resourcesManager.backend.resourcesManager.services.PanneService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pannes")
@CrossOrigin("*")
public class PanneController {

    private final PanneService panneService;

    public PanneController(PanneService panneService) {
        this.panneService = panneService;
    }

    @GetMapping("")
    public List<Panne> getAllPannes() {
        return panneService.getAllPannes();
    }
    @GetMapping("/pannesAvecRessources")
    public List<PanneAvecRessource> getNotTreatedPanne(){
        return panneService.getNotTreatedPanne();
    }

    @PostMapping()
    Panne addPanne(@RequestBody Panne panne) {
        return panneService.addPanne(panne);
    }

    @PutMapping("/{id}")
    Panne updatePanne(@PathVariable Long id, @RequestBody Panne panne) {
        panne.setId(id);
        return panneService.updatePanne(panne);
    }

    @DeleteMapping("/{id}")
    void deletePanne(@PathVariable Long id) {
        panneService.deletePanne(id);
    }

    @GetMapping("/membreDepartement/{id}")
    List<Panne> getPannesMembreDepartement(@PathVariable String id) {
        return panneService.getPannesMembreDepartement(id);
    }

    @GetMapping("/pannesNotTreated")
    List<Panne> getPannesNotTreated() {
        return panneService.getPannesNotTreated();
    }

    @GetMapping("/constats")
    List<Panne> getPanneWithConstatNotNullAndDemandeNull() {
        return panneService.getPanneWithConstatNotNullAndDemandeNull();
    }

    @GetMapping("/demandes")
    List<Panne> getPanneWithDemandeNotNull() {
        return panneService.getPanneWithDemandeNotNull();
    }

}
