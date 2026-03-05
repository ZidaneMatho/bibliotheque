package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.User;
import com.example.bibliotheque.services.UserService;
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
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Récupère tous les utilisateurs.
     * @return liste des utilisateurs
     */
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
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Récupère un utilisateur par son email.
     * @param email l'email de l'utilisateur
     * @return l'utilisateur trouvé
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'email: " + email));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Crée un nouvel utilisateur.
     * @param user les données de l'utilisateur à créer
     * @return l'utilisateur créé
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
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
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Supprime un utilisateur.
     * @param id l'identifiant de l'utilisateur à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un email existe déjà.
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     */
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}