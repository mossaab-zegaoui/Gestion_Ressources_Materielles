package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Imprimante;
import com.resourcesManager.backend.resourcesManager.services.ImprimanteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/imprimantes")
@CrossOrigin("*")
public class ImprimanteController {

    private final ImprimanteService imprimanteService;

    public ImprimanteController(ImprimanteService imprimanteService) {
        this.imprimanteService = imprimanteService;
    }

    @GetMapping()
    List<Imprimante> getAllImprimantes() {
        return imprimanteService.getAllImprimantes();
    }

    @GetMapping("/membreDepartement/{id}")
    List<Imprimante> getImprimantesByMembreDepartement(@PathVariable String id) {
        return imprimanteService.getImprimantesByMembreDepartement(id);
    }

    @GetMapping("/departement/{id}")
    List<Imprimante> getImprimantesByDepartement(@PathVariable Long id) {
        return imprimanteService.getImprimantesByDepartement(id);
    }

    @GetMapping("/fournisseur/{id}")
    List<Imprimante> getImprimantesByFournisseur(@PathVariable String id) {
        return imprimanteService.getImprimantesByFournisseur(id);
    }

    @GetMapping("/{id}")
    Imprimante getImprimante(@PathVariable Long id) {
        return imprimanteService.getImprimante(id);
    }

    @PutMapping("/{id}")
    Imprimante updateImprimante(@PathVariable Long id, @RequestBody Imprimante imprimante) {
        return imprimanteService.updateImprimante(imprimante);
    }

    @DeleteMapping("/{id}")
    void deleteOrdinateur(@PathVariable Long id) {
        imprimanteService.deleteImprimante(id);
    }

    @GetMapping("/listeDisponible")
    List<Imprimante> getImprimanteDisponible(){
        return imprimanteService.getImprimantesDisponibles();
    }
    @GetMapping("/listeLivree")
    List<Imprimante> getImprimanteLivree(){
        return imprimanteService.getImprimantesLivrees();
    }

}