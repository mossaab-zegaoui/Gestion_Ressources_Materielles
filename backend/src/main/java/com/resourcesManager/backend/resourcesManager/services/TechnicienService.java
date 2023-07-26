package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.Technicien;

import java.util.List;

public interface TechnicienService {
    Technicien addTechnicien(Technicien technicien);
    Technicien updateTechnicien(Technicien technicien);
    void deleteTechnicien(String id);
    List<Technicien> getAllTechniciens();
    Technicien getTechnicienById(String id);
}
