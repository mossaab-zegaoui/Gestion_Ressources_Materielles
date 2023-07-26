package com.resourcesManager.backend.resourcesManager;


import com.resourcesManager.backend.resourcesManager.model.Role;
import com.resourcesManager.backend.resourcesManager.model.User;

import com.resourcesManager.backend.resourcesManager.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

import static com.resourcesManager.backend.resourcesManager.enums.RoleName.*;

@SpringBootApplication
public class ResourcesManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResourcesManagerApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(AuthenticationService authenticationService) {
        return args -> {
//            Stream.of(ROLE_USER.name(), ROLE_RESPONSABLE.name(), ROLE_FOURNISSEUR.name(), ROLE_ENSEIGNANT.name(), ROLE_TECHNICIEN.name(),
//                    ROLE_CHEF_DEPARTEMENT.name()).forEach(name -> {
//                Role
//                        role = new Role();
//                role.setNomRole(name);
//                authenticationService.saveRole(role.getNomRole());
//            });
//            Role responsableRole = authenticationService.getRole(ROLE_RESPONSABLE.name());
//            User responsable = new User();
//            responsable.setUsername("responsable");
//            responsable.setPassword("123");
//            responsable.setEmail("responsable@gmail.com");
//            responsable.setRoles(List.of(responsableRole));
//            responsable.setEnabled(true);
//            authenticationService.saveResponsable(responsable);
        };
    }


}
