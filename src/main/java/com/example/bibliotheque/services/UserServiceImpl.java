package com.example.bibliotheque.services;

import com.example.bibliotheque.models.User;
import com.example.bibliotheque.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation des services de gestion des utilisateurs.
 * Contient la logique métier pour les opérations sur les utilisateurs.
 */
@Service
public class UserServiceImpl implements UserService {

    /** Repository pour accéder à la base de données des utilisateurs. */
    @Autowired
    private UserRepository userRepository;

    /**
     * Crée un nouvel utilisateur.
     *
     * @param user L'utilisateur à créer
     * @return L'utilisateur sauvegardé avec son ID généré
     */
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retourne la liste de tous les utilisateurs.
     *
     * @return Liste de tous les utilisateurs
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Recherche un utilisateur par son ID.
     *
     * @param id L'identifiant de l'utilisateur
     * @return L'utilisateur trouvé ou Optional vide
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Recherche un utilisateur par son email.
     *
     * @param email L'email de l'utilisateur
     * @return L'utilisateur trouvé ou Optional vide
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @param id L'identifiant de l'utilisateur à modifier
     * @param userDetails Les nouvelles informations
     * @return L'utilisateur mis à jour
     * @throws RuntimeException si l'utilisateur n'est pas trouvé
     */
    @Override
    public User updateUser(Long id, User userDetails) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));

        // Mettre à jour les champs
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setRole(userDetails.getRole());

        // Sauvegarder les modifications
        return userRepository.save(user);
    }

    /**
     * Supprime un utilisateur par son ID.
     *
     * @param id L'identifiant de l'utilisateur à supprimer
     */
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Vérifie si un email existe déjà.
     *
     * @param email L'email à vérifier
     * @return true si l'email existe, false sinon
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}