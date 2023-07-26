package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.AppelOffre;
import com.resourcesManager.backend.resourcesManager.services.AppelOffreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appelOffre")
@CrossOrigin("*")
public class AppelOffreController {
    private final AppelOffreService appelOffreService;

    public AppelOffreController(AppelOffreService appelOffreService) {
        this.appelOffreService = appelOffreService;
    }

    @GetMapping
    public List<AppelOffre> getAllAppelOffre() {
        return appelOffreService.getAllAppelOffre();
    }


    @GetMapping("/{id}")
    AppelOffre getAppelOffre(@PathVariable Long id) {
        return appelOffreService.getAppelOffreById(id);
    }

    @PostMapping("/publier")
    void publierAppelOffre(@RequestBody AppelOffre appelOffre) {
        appelOffreService.publierAppelOffre(appelOffre);
    }

    @DeleteMapping("/{id}")
    void deleteAppelOffre(@PathVariable Long id) {
        appelOffreService.deleteAppelOffre(id);
    }

}