package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Administrator;
import com.example.bibliotheque.repository.AdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des administrateurs.
 * Contient la logique métier pour les opérations sur les administrateurs.
 */
@Service
public class AdministratorServiceImpl implements AdministratorService {

    /** Repository pour accéder à la base de données des administrateurs. */
    @Autowired
    private AdministratorRepository administratorRepository;

    /**
     * {@inheritDoc}
     *
     * Sauvegarde un nouvel administrateur.
     * Le matricule (employeeId) doit être unique.
     */
    @Override
    public Administrator createAdministrator(Administrator admin) {
        // Vérifier si le matricule existe déjà
        if (admin.getEmployeeId() != null &&
                administratorRepository.findByEmployeeId(admin.getEmployeeId()).isPresent()) {
            throw new RuntimeException("Un administrateur avec le matricule '" + admin.getEmployeeId() + "' existe déjà");
        }
        return administratorRepository.save(admin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Administrator> getAllAdministrators() {
        return administratorRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Administrator> getAdministratorById(Long id) {
        return administratorRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Administrator> getAdministratorByEmployeeId(String employeeId) {
        return administratorRepository.findByEmployeeId(employeeId);
    }

    /**
     * {@inheritDoc}
     */

    /**
     * {@inheritDoc}
     *
     * Met à jour les informations d'un administrateur existant.
     * @throws RuntimeException si l'administrateur n'existe pas
     */
    @Override
    public Administrator updateAdministrator(Long id, Administrator adminDetails) {
        Administrator admin = administratorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'id: " + id));

        // Vérifier si le nouveau matricule est déjà pris
        if (adminDetails.getEmployeeId() != null &&
                !adminDetails.getEmployeeId().equals(admin.getEmployeeId())) {
            administratorRepository.findByEmployeeId(adminDetails.getEmployeeId())
                    .ifPresent(a -> {
                        throw new RuntimeException("Un administrateur avec le matricule '" + adminDetails.getEmployeeId() + "' existe déjà");
                    });
        }

        // Mise à jour des champs spécifiques
        if (adminDetails.getAccessLevel() != null) {
            admin.setAccessLevel(adminDetails.getAccessLevel());
        }
        if (adminDetails.getDepartment() != null) {
            admin.setDepartment(adminDetails.getDepartment());
        }
        if (adminDetails.getEmployeeId() != null) {
            admin.setEmployeeId(adminDetails.getEmployeeId());
        }

        // Mise à jour des champs hérités de User
        if (adminDetails.getFirstName() != null) {
            admin.setFirstName(adminDetails.getFirstName());
        }
        if (adminDetails.getLastName() != null) {
            admin.setLastName(adminDetails.getLastName());
        }
        if (adminDetails.getEmail() != null) {
            admin.setEmail(adminDetails.getEmail());
        }
        if (adminDetails.getPhoneNumber() != null) {
            admin.setPhoneNumber(adminDetails.getPhoneNumber());
        }

        return administratorRepository.save(admin);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteAdministrator(Long id) {
        if (!administratorRepository.existsById(id)) {
            throw new RuntimeException("Administrateur non trouvé avec l'id: " + id);
        }
        administratorRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByEmployeeId(String employeeId) {
        return administratorRepository.findByEmployeeId(employeeId).isPresent();
    }
}