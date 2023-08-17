package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.mapper.MapUserToUserDetails;
import com.resourcesManager.backend.resourcesManager.model.Besoin;
import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.services.BesoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/besoins")
@CrossOrigin("*")
public class BesoinController {

    private final BesoinService besoinService;

    public BesoinController(BesoinService besoinService) {
        this.besoinService = besoinService;
    }

    @GetMapping("")
    public ResponseEntity<List<Besoin>> getAllBesoins() {

        return ResponseEntity.ok(besoinService.getAllBesoins());
    }

    @PostMapping("")
    ResponseEntity<Besoin> save(@RequestBody Besoin besoin, @AuthenticationPrincipal MapUserToUserDetails userDetails) {
        return ResponseEntity.ok(besoinService.saveBesoin(besoin, userDetails.getUser().getId()));
    }

    @GetMapping("departement")
    ResponseEntity<List<Besoin>> getAllBesoinDepartement(@AuthenticationPrincipal MapUserToUserDetails userDetails) {
        return ResponseEntity.ok(besoinService.getBesoinsByDepartement(userDetails.getUser().getId()));
    }

    @DeleteMapping("/membreDepartement/{id}")
    ResponseEntity<?> deleteBesoinOfMembre(String id) {
        besoinService.deleteBesoinOfMembre(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/membreDepartement/{id}")
    ResponseEntity<List<Besoin>> getBesoinsMembreDepartement(@PathVariable String id) {
        return ResponseEntity.ok(besoinService.getBesoinsByMembreDepartement(id));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteBesoin(@PathVariable Long id) {
        besoinService.deleteBesoin(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/membreDepartement/notInOffre/{id}")
    ResponseEntity<List<Besoin>> getBesoinsDepartementNotInAppelOffre(@PathVariable Long id) {
        return ResponseEntity.ok(besoinService.getBesoinsDepartementNotInAppelOffre(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Besoin> updateBesoin(@PathVariable Long id, @RequestBody Besoin besoin) {
        besoin.setId(id);
        return ResponseEntity.ok(besoinService.updateBesoin(besoin));
    }

    @GetMapping("/departement/notInOffre/{id}")
    ResponseEntity<Besoin> getBesoinMembreDepartementNotInAppelOffre(String id) {
        return ResponseEntity.ok(besoinService.getBesoinsDisponiblesByIdMembre(id));
    }

    @PutMapping("/addedInoffre/{id}")
    ResponseEntity<?> besoinAddedInAppelOffre(@PathVariable Long id) {
        besoinService.besoinAddedInAppelOffre(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/NotInAppelOffre")
    List<Besoin> getBesoinsNotInAppelOffre(){
        return besoinService.getBesoinsNotInAppelOffre();
    }

}
