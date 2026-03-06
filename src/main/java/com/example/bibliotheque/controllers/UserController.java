package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.User;
import com.example.bibliotheque.services.UserService;
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
 * Contrôleur REST pour la gestion des utilisateurs.
 * Toutes les routes commencent par /api/users
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "API pour la gestion des utilisateurs")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupère tous les utilisateurs.
     * @return liste des utilisateurs
     */
    @Operation(summary = "Liste tous les utilisateurs",
            description = "Retourne la liste complète de tous les utilisateurs enregistrés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Récupère un utilisateur par son ID.
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur trouvé
     */
    @Operation(summary = "Recherche un utilisateur par son ID",
            description = "Retourne un utilisateur spécifique à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID de l'utilisateur à rechercher", required = true)
            @PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Récupère un utilisateur par son email.
     * @param email l'email de l'utilisateur
     * @return l'utilisateur trouvé
     */
    @Operation(summary = "Recherche un utilisateur par son email",
            description = "Retourne un utilisateur spécifique à partir de son adresse email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur trouvé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
            @Parameter(description = "Email de l'utilisateur à rechercher", required = true)
            @PathVariable String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Crée un nouvel utilisateur.
     * @param user les données de l'utilisateur à créer
     * @return l'utilisateur créé
     */
    @Operation(summary = "Crée un nouvel utilisateur",
            description = "Ajoute un nouvel utilisateur dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Email déjà existant")
    })
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "Données de l'utilisateur à créer", required = true)
            @RequestBody User user) {
        // Vérifier si l'email existe déjà
        if (userService.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec l'email '" + user.getEmail() + "' existe déjà");
        }
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Met à jour un utilisateur existant.
     * @param id l'identifiant de l'utilisateur à modifier
     * @param user les nouvelles données
     * @return l'utilisateur mis à jour
     */
    @Operation(summary = "Met à jour un utilisateur",
            description = "Modifie les informations d'un utilisateur existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Utilisateur mis à jour"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID de l'utilisateur à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de l'utilisateur", required = true)
            @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Supprime un utilisateur.
     * @param id l'identifiant de l'utilisateur à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @Operation(summary = "Supprime un utilisateur",
            description = "Supprime définitivement un utilisateur de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID de l'utilisateur à supprimer", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un email existe déjà.
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     */
    @Operation(summary = "Vérifie si un email existe",
            description = "Permet de savoir si une adresse email est déjà utilisée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(
            @Parameter(description = "Email à vérifier", required = true)
            @PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}