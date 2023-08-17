package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.AppelOffre;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.AppelOffreRepository;
import com.resourcesManager.backend.resourcesManager.repository.BesoinRepository;
import com.resourcesManager.backend.resourcesManager.services.AppelOffreService;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Service
public class AppelOffreImpl implements AppelOffreService {
    private final AppelOffreRepository appelOffreRepository;
    private final BesoinRepository besoinRepository;

    public AppelOffreImpl(AppelOffreRepository appelOffreRepository,
                          BesoinRepository besoinRepository) {
        this.appelOffreRepository = appelOffreRepository;
        this.besoinRepository = besoinRepository;
    }


    @Override
    public AppelOffre getAppelOffreById(Long id) {
        AppelOffre appelOffre = appelOffreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Appel d'offre with id: " + id + " not found"));

        return appelOffre;
    }

    @Override
    public List<AppelOffre> getAllAppelOffre() {
        return appelOffreRepository.findAll();
    }

    @Override
    public void publierAppelOffre(AppelOffre appelOffre) {
        Optional.ofNullable(appelOffre.getBesoins())
                .stream()
                .flatMap(Collection::stream)
                .peek(b -> b.setBesoinInAppelOffre(true))
                .forEach(besoinRepository::save);
        appelOffre.setDatePub(Date.valueOf(LocalDate.now()));
        appelOffreRepository.save(appelOffre);
    }

    @Override
    public void deleteAppelOffre(Long id) {
        AppelOffre appelOffre = appelOffreRepository.findAppelOffreById(id)
                .orElseThrow(() -> new NotFoundException("Appel d'offre with id: " + id + " not found"));

        appelOffre.getBesoins()
                .stream()
                .peek(besoin -> besoin.setBesoinInAppelOffre(false))
                .forEach(besoinRepository::save);

        appelOffreRepository.deleteById(id);
    }

}