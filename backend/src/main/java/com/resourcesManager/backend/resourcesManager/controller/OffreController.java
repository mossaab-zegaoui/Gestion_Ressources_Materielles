package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Offre;
import com.resourcesManager.backend.resourcesManager.services.OffreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/offre")
@CrossOrigin("*")
public class OffreController {

    private final OffreService offreService;

    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }

    @GetMapping("")
    List<Offre> getAllOffres(){
        return offreService.getAllOffres();
    }

    @GetMapping("/Fournisseur/{id}")
    List<Offre> getOffresFournisseur(@PathVariable String id){
        return offreService.getOffreByFournisseur(id);
    }

    @GetMapping("/AppelOffre/{id}")
    List<Offre> getOffresAppelOffre(@PathVariable Long id){
        return offreService.getOffreByAppelOffre(id);
    }

    @GetMapping("/{id}")
    Offre getOffre(@PathVariable Long id){
        return offreService.getOffre(id);
    }

    @PostMapping("")
    Offre creerOffre(@RequestBody Offre offre){
        return  offreService.creerOffre(offre);
    }

    @PutMapping("/{id}")
    Offre updateOffre(@PathVariable Long id,@RequestBody Offre offre){
        return offreService.updateOffre(offre);
    }
    @DeleteMapping("/{id}")
    void deleteOffre(@PathVariable Long id){
        offreService.deleteOffre(id);
    }

    @PutMapping("/accepter/{id}")
    void accepterOffre(@PathVariable Long id,@RequestBody Offre offre){
        offreService.accepterOffre(offre);
    }

}