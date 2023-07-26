package com.resourcesManager.backend.resourcesManager.services;

import com.resourcesManager.backend.resourcesManager.model.AppelOffre;
import com.resourcesManager.backend.resourcesManager.model.Besoin;
import com.resourcesManager.backend.resourcesManager.repository.AppelOffreRepository;
import com.resourcesManager.backend.resourcesManager.repository.BesoinRepository;
import com.resourcesManager.backend.resourcesManager.services.impl.AppelOffreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppelOffreImplTest {
    @Mock
    private AppelOffreRepository appelOffreRepository;
    @Mock
    private BesoinRepository besoinRepository;
    @InjectMocks
    private AppelOffreImpl appelOffreSevice;
    private Besoin BESOIN_1;
    private Besoin BESOIN_2;
    private AppelOffre appelOffre;

    @BeforeEach
    void setUp() {
        BESOIN_1 = new Besoin();
        BESOIN_1.setIdDepartement(1L);
        BESOIN_1.setIsBesoinInAppelOffre(false);
        BESOIN_2 = new Besoin();
        BESOIN_2.setIdDepartement(2L);
        appelOffre = new AppelOffre();
        appelOffre.setId(1L);
        BESOIN_2.setIsBesoinInAppelOffre(false);
        appelOffre.setBesoins(List.of(BESOIN_1, BESOIN_2));
        appelOffre.setIsAffected(false);
    }

    @Test
    void publierAppelOffre_ShouldReturnSavedAppelOffre() {

        when(besoinRepository.save(BESOIN_1)).thenReturn(BESOIN_1);
        when(besoinRepository.save(BESOIN_2)).thenReturn(BESOIN_2);
        when(appelOffreRepository.save(appelOffre)).thenReturn(appelOffre);
//
        appelOffreSevice.publierAppelOffre(appelOffre);
//
        assertEquals(true, BESOIN_1.getIsBesoinInAppelOffre());
        assertEquals(true, BESOIN_2.getIsBesoinInAppelOffre());
        assertEquals(2, appelOffre.getBesoins().size());
    }

    @Test
    void deleteAppelOffre_ShouldDeleteAppelOffre() {
        Long id = 1L;
        BESOIN_1.setIsBesoinInAppelOffre(true);
        BESOIN_2.setIsBesoinInAppelOffre(true);

        when(appelOffreRepository.findAppelOffreById(id)).thenReturn(Optional.ofNullable(appelOffre));
        when(besoinRepository.save(BESOIN_1)).thenReturn(BESOIN_1);
        when(besoinRepository.save(BESOIN_2)).thenReturn(BESOIN_2);
//
        appelOffreSevice.deleteAppelOffre(id);
//
        assertFalse(BESOIN_1.getIsBesoinInAppelOffre());
        assertFalse(BESOIN_2.getIsBesoinInAppelOffre());

    }
}