package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Administrator;
import com.example.bibliotheque.services.AdministratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des administrateurs.
 * Toutes les routes commencent par /api/administrators
 */
@RestController
@RequestMapping("/api/administrators")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    /**
     * Récupère tous les administrateurs.
     * GET /api/administrators
     * @return liste des administrateurs
     */
    @GetMapping
    public ResponseEntity<List<Administrator>> getAllAdministrators() {
        List<Administrator> administrators = administratorService.getAllAdministrators();
        return new ResponseEntity<>(administrators, HttpStatus.OK);
    }

    /**
     * Récupère un administrateur par son ID.
     * GET /api/administrators/{id}
     * @param id l'identifiant de l'administrateur
     * @return l'administrateur trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(@PathVariable Long id) {
        Administrator admin = administratorService.getAdministratorById(id)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec l'id: " + id));
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * Récupère un administrateur par son matricule.
     * GET /api/administrators/employee/{employeeId}
     * @param employeeId le matricule de l'administrateur
     * @return l'administrateur trouvé
     */
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Administrator> getAdministratorByEmployeeId(@PathVariable String employeeId) {
        Administrator admin = administratorService.getAdministratorByEmployeeId(employeeId)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé avec le matricule: " + employeeId));
        return new ResponseEntity<>(admin, HttpStatus.OK);
    }

    /**
     * Crée un nouvel administrateur.
     * POST /api/administrators
     * @param admin les données de l'administrateur à créer
     * @return l'administrateur créé
     */
    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(@RequestBody Administrator admin) {
        // Vérifier si le matricule existe déjà
        if (admin.getEmployeeId() != null &&
                administratorService.existsByEmployeeId(admin.getEmployeeId())) {
            throw new RuntimeException("Un administrateur avec le matricule '" + admin.getEmployeeId() + "' existe déjà");
        }
        Administrator newAdmin = administratorService.createAdministrator(admin);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    /**
     * Met à jour un administrateur existant.
     * PUT /api/administrators/{id}
     * @param id l'identifiant de l'administrateur à modifier
     * @param admin les nouvelles données
     * @return l'administrateur mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(@PathVariable Long id, @RequestBody Administrator admin) {
        Administrator updatedAdmin = administratorService.updateAdministrator(id, admin);
        return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
    }

    /**
     * Supprime un administrateur.
     * DELETE /api/administrators/{id}
     * @param id l'identifiant de l'administrateur à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(@PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un matricule existe déjà.
     * GET /api/administrators/exists/employee/{employeeId}
     * @param employeeId le matricule à vérifier
     * @return true si le matricule existe, false sinon
     */
    @GetMapping("/exists/employee/{employeeId}")
    public ResponseEntity<Boolean> checkEmployeeIdExists(@PathVariable String employeeId) {
        boolean exists = administratorService.existsByEmployeeId(employeeId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}