package com.example.bibliotheque.services;

import com.example.bibliotheque.models.User;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des utilisateurs.
 */
public interface UserService {

    /** Crée un nouvel utilisateur. */
    User createUser(User user);

    /** Retourne tous les utilisateurs. */
    List<User> getAllUsers();

    /** Recherche un utilisateur par son ID. */
    Optional<User> getUserById(Long id);

    /** Recherche un utilisateur par son email. */
    Optional<User> getUserByEmail(String email);

    /** Met à jour un utilisateur existant. */
    User updateUser(Long id, User userDetails);

    /** Supprime un utilisateur. */
    void deleteUser(Long id);

    /** Vérifie si un email existe déjà. */
    boolean existsByEmail(String email);
}