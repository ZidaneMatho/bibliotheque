package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Administrator;
import com.example.bibliotheque.services.AdministratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Administrateurs", description = "API pour la gestion des administrateurs (bibliothécaires)")
public class AdministratorController {

    @Autowired
    private AdministratorService administratorService;

    /**
     * Récupère tous les administrateurs.
     * GET /api/administrators
     * @return liste des administrateurs
     */
    @Operation(summary = "Liste tous les administrateurs",
            description = "Retourne la liste complète de tous les administrateurs (réservé aux administrateurs)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)")
    })
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
    @Operation(summary = "Recherche un administrateur par son ID",
            description = "Retourne un administrateur spécifique à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Administrator> getAdministratorById(
            @Parameter(description = "ID de l'administrateur à rechercher", required = true)
            @PathVariable Long id) {
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
    @Operation(summary = "Recherche un administrateur par son matricule",
            description = "Retourne un administrateur spécifique à partir de son matricule unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Administrator> getAdministratorByEmployeeId(
            @Parameter(description = "Matricule de l'administrateur à rechercher", required = true, example = "ADM001")
            @PathVariable String employeeId) {
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
    @Operation(summary = "Crée un nouvel administrateur",
            description = "Ajoute un nouvel administrateur dans la base de données (réservé aux super-administrateurs)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Administrateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)"),
            @ApiResponse(responseCode = "409", description = "Matricule déjà existant")
    })
    @PostMapping
    public ResponseEntity<Administrator> createAdministrator(
            @Parameter(description = "Données de l'administrateur à créer", required = true)
            @RequestBody Administrator admin) {
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
    @Operation(summary = "Met à jour un administrateur",
            description = "Modifie les informations d'un administrateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Administrateur mis à jour"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé"),
            @ApiResponse(responseCode = "409", description = "Matricule déjà utilisé par un autre administrateur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Administrator> updateAdministrator(
            @Parameter(description = "ID de l'administrateur à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de l'administrateur", required = true)
            @RequestBody Administrator admin) {
        Administrator updatedAdmin = administratorService.updateAdministrator(id, admin);
        return new ResponseEntity<>(updatedAdmin, HttpStatus.OK);
    }

    /**
     * Supprime un administrateur.
     * DELETE /api/administrators/{id}
     * @param id l'identifiant de l'administrateur à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @Operation(summary = "Supprime un administrateur",
            description = "Supprime définitivement un administrateur de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Administrateur supprimé avec succès"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)"),
            @ApiResponse(responseCode = "404", description = "Administrateur non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrator(
            @Parameter(description = "ID de l'administrateur à supprimer", required = true)
            @PathVariable Long id) {
        administratorService.deleteAdministrator(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un matricule existe déjà.
     * GET /api/administrators/exists/employee/{employeeId}
     * @param employeeId le matricule à vérifier
     * @return true si le matricule existe, false sinon
     */
    @Operation(summary = "Vérifie si un matricule existe",
            description = "Permet de savoir si un matricule d'administrateur est déjà utilisé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)")
    })
    @GetMapping("/exists/employee/{employeeId}")
    public ResponseEntity<Boolean> checkEmployeeIdExists(
            @Parameter(description = "Matricule à vérifier", required = true, example = "ADM001")
            @PathVariable String employeeId) {
        boolean exists = administratorService.existsByEmployeeId(employeeId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}