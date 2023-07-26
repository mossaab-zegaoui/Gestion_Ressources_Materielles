package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.AppelOffre;
import com.resourcesManager.backend.resourcesManager.model.NotifFournisseur;
import com.resourcesManager.backend.resourcesManager.model.Offre;
import com.resourcesManager.backend.resourcesManager.model.Ressource;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.repository.AppelOffreRepository;
import com.resourcesManager.backend.resourcesManager.repository.OffreRepository;
import com.resourcesManager.backend.resourcesManager.repository.RessourceRepository;
import com.resourcesManager.backend.resourcesManager.services.NotifFournisseurService;
import com.resourcesManager.backend.resourcesManager.services.OffreService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class OffreServiceImpl implements OffreService {
    private final OffreRepository offreRepository;
    private final AppelOffreRepository appelOffreRepository;
    private final RessourceRepository ressourceRepository;
    private final NotifFournisseurService notifFournisseurService;

    @Override
    public Offre getOffre(Long id) {
        Offre offre = offreRepository.findOffreById(id)
                .orElseThrow(() -> new NotFoundException("offre with id" + id + " not found"));
        return offre;
    }

    @Override
    public List<Offre> getOffreByAppelOffre(Long idAppelOffre) {
        return offreRepository.findOffreByIdAppelOffre(idAppelOffre);
    }

    @Override
    public List<Offre> getOffreByFournisseur(String idFournisseur) {
        return offreRepository.findOffreByIdFournisseur(idFournisseur);

    }

    @Override
    public Offre creerOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    @Override
    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    @Override
    public Offre updateOffre(Offre offre) {
        return offreRepository.save(offre);
    }

    @Override
    public void deleteOffre(Long id) {
        Optional<Offre> offre = offreRepository.findById(id);
        if (offre.isEmpty()) throw new NotFoundException("offre with id " + id + " not found");
        offreRepository.deleteById(id);
    }


    @Override
    public void accepterOffre(Offre offre) {
        offre.setIsAffected(true);
        offre.setIsWaiting(false);
        offre.setIsRejected(false);
        Optional<AppelOffre> appelOffre = this.appelOffreRepository.findAppelOffreById(offre.getIdAppelOffre());
        appelOffre.get().setIsAffected(true);
        offreRepository.save(offre);
        appelOffreRepository.save(appelOffre.get());
        offre.getRessources().forEach(rf -> {
            Ressource ressource = ressourceRepository.findById(rf.getIdRessource()).orElseThrow();
            ressource.setPrix(rf.getPrix());
            ressource.setMarque(rf.getMarque());
            ressource.setIdFournisseur(offre.getIdFournisseur());
            ressourceRepository.save(ressource);
        });
        appelOffre.get().getBesoins()
                .forEach(besoin -> besoin.setDateAffectation(Date.valueOf(LocalDate.now())));

        List<Offre> offresRejected = offreRepository.findOffreByIdAppelOffreAndIsAffectedIsFalse(offre.getIdAppelOffre());
        offresRejected.forEach(offreRejected -> {
            offreRejected.setIsRejected(true);
            offre.setIsWaiting(false);
            offreRepository.save(offreRejected);
        });

        createNotificationFournisseur(appelOffre.get(), offre.getId());

    }

    private void createNotificationFournisseur(AppelOffre appelOffre, Long offreId) {
        List<Offre> offres = offreRepository.findOffreByIdAppelOffre(appelOffre.getId());
        offres.forEach(offre -> {
            String idFour = offre.getIdFournisseur();
            NotifFournisseur notifFournisseur = new NotifFournisseur();
            notifFournisseur.setIsSeen(false);
            notifFournisseur.setDateOffre(offre.getDateDebut());
            notifFournisseur.setIdFournisseur(idFour);
            notifFournisseur.setIsAccepted(offreId.equals(offre.getId()));
            notifFournisseurService.addNotifFournisseur(notifFournisseur);
        });
    }

}