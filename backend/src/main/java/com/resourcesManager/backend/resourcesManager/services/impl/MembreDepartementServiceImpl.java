package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Departement;
import com.resourcesManager.backend.resourcesManager.model.MembreDepartement;
import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.exceptions.EntityAlreadyExistsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
import com.resourcesManager.backend.resourcesManager.model.User;
import com.resourcesManager.backend.resourcesManager.repository.DepartementRepository;
import com.resourcesManager.backend.resourcesManager.repository.MembreDepartementRepository;
import com.resourcesManager.backend.resourcesManager.repository.RoleRepository;
import com.resourcesManager.backend.resourcesManager.repository.UserRepository;
import com.resourcesManager.backend.resourcesManager.services.MembreDepartementService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.resourcesManager.backend.resourcesManager.enums.RoleName.*;

@Service
@Transactional
@AllArgsConstructor

public class MembreDepartementServiceImpl implements MembreDepartementService {

    private final MembreDepartementRepository membreDepartementRepository;
    private final DepartementRepository departementRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<MembreDepartement> getAllMembresDepartement() {
        return membreDepartementRepository.findAll();
    }

    public MembreDepartement getMembreDepartement(String id) {
        return membreDepartementRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Le membre departement avec l'id = " + id + " est introuvable")
        );
    }

    @Override
    public MembreDepartement saveMembre(MembreDepartement membre) {
        if (!isMembreValid(membre)) return null;
        membre.setPassword(passwordEncoder.encode(membre.getPassword()));
        String roleName = membre.getRoles().stream().findFirst().get().getNomRole();
        return addRoleToMembre(membre, roleName);
    }


    @Override
    public MembreDepartement updateMembreDepartement(MembreDepartement membre) {
        String roleName = membre.getRoles().stream().findFirst().get().getNomRole();
        return addRoleToMembre(membre, roleName);
    }

    @Override
    public List<MembreDepartement> getMembresByIdDepartement(Long idDepartement) {

        return membreDepartementRepository.getMembreDepartementByDepartementId(idDepartement);
    }

    @Override
    public void deleteMembreDepartement(String id) {
        MembreDepartement membreDepartement = membreDepartementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("membre departement" + id + " not found"));
        membreDepartementRepository.delete(membreDepartement);
    }


    private boolean isMembreValid(MembreDepartement membre) {
        if (userRepository.existsByUsername(membre.getUsername()))
            throw new EntityAlreadyExistsException("username:" + membre.getUsername() + " already exists");
        if (userRepository.existsByEmailIgnoreCase(membre.getEmail()))
            throw new EntityAlreadyExistsException("email:" + membre.getEmail() + " already exists");
        return true;
    }

    private MembreDepartement addRoleToMembre(MembreDepartement membre, String roleName) {
        Role role = roleRepository.findRoleByNomRole(roleName)
                .orElseThrow(() -> new NotFoundException(roleName + " does not exist"));
        membre.setRoles(List.of(role));
        return membreDepartementRepository.save(membre);
    }
}
