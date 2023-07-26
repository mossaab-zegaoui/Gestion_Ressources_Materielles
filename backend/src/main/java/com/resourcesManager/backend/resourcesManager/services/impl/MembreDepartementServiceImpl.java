package com.resourcesManager.backend.resourcesManager.services.impl;

import com.resourcesManager.backend.resourcesManager.model.Departement;
import com.resourcesManager.backend.resourcesManager.model.MembreDepartement;
import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.exceptions.EntityAlreadyExistsException;
import com.resourcesManager.backend.resourcesManager.exceptions.NotFoundException;
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
        if (userRepository.existsByUsername(membre.getUsername()))
            throw new EntityAlreadyExistsException("Usename:" + membre.getUsername() + " already exists");
        if (userRepository.existsByEmailIgnoreCase(membre.getEmail()))
            throw new EntityAlreadyExistsException("email:" + membre.getEmail() + " already exists");
        Optional<Departement> departement = departementRepository.findById(membre.getDepartement().getId());

        membre.setEmail(membre.getEmail());
        membre.setPassword(passwordEncoder.encode(membre.getPassword()));
        membre.setDepartement(departement.get());

        return membreDepartementRepository.save(membre);
    }

    @Override
    public MembreDepartement updateMembreDepartement(MembreDepartement membreDepartement) {
        return membreDepartementRepository.save(membreDepartement);
    }

    @Override
    public List<MembreDepartement> getMembresByIdDepartement(Long idDepartement) {

        return this.membreDepartementRepository.getMembreDepartementByDepartementId(idDepartement);
    }

    @Override
    public void deleteMembreDepartement(String id) {
        MembreDepartement membreDepartement = membreDepartementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("membre departement" + id + " not found"));
        membreDepartementRepository.delete(membreDepartement);
    }

    private Role getRole(String roleName) {
        return roleRepository.findRoleByNomRole(roleName)
                .orElseThrow(() -> new NotFoundException("role: " + roleName + " NOT FOUND !!"));

    }

}
