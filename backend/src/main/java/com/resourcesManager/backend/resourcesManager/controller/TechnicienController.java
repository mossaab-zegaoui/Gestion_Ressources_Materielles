package com.resourcesManager.backend.resourcesManager.controller;

import com.resourcesManager.backend.resourcesManager.model.Technicien;
import com.resourcesManager.backend.resourcesManager.services.impl.TechnicienServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technicien")
@CrossOrigin("*")
public class TechnicienController {

    private final TechnicienServiceImpl technicienServiceImpl;

    public TechnicienController(TechnicienServiceImpl technicienServiceImpl) {
        this.technicienServiceImpl = technicienServiceImpl;
    }

    @GetMapping()
    public List<Technicien> getAllTechniciens() {
        return technicienServiceImpl.getAllTechniciens();
    }

    @GetMapping("/{id}")
    public Technicien getTechnicienById(@PathVariable String id) {
        return technicienServiceImpl.getTechnicienById(id);
    }

    @PostMapping
    public Technicien addTechnicien(@RequestBody Technicien technicien) {
        return technicienServiceImpl.addTechnicien(technicien);
    }

    @PutMapping("/{id}")
    public Technicien updateTechnicien(@PathVariable String id, @RequestBody Technicien technicien) {
        technicien.setId(id);
        return technicienServiceImpl.updateTechnicien(technicien);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMTechnicien(@PathVariable String id) {
        technicienServiceImpl.deleteTechnicien(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
