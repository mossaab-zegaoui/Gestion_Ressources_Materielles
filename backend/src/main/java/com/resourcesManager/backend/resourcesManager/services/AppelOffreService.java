package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.AppelOffre;

import java.util.List;

public interface AppelOffreService  {

    AppelOffre getAppelOffreById(Long id);
    List<AppelOffre> getAllAppelOffre();
    void publierAppelOffre(AppelOffre appelOffre);
    void deleteAppelOffre(Long id);

}