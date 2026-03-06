package com.example.bibliotheque.services;

import com.example.bibliotheque.models.User;
import com.example.bibliotheque.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des utilisateurs.
 *
 * Cette classe contient toute la logique métier liée aux utilisateurs :
 * - Création de compte avec cryptage du mot de passe
 * - Recherche et consultation des utilisateurs
 * - Mise à jour des informations
 * - Suppression de comptes
 * - Vérification d'unicité des emails
 *
 * Elle fait le lien entre les contrôleurs (API) et le repository (base de données).
 *
 * @see UserService
 * @see UserRepository
 * @see PasswordEncoder
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Repository pour accéder à la table "users" en base de données.
     * Injecté par constructeur grâce à Lombok (@RequiredArgsConstructor).
     */
    private final UserRepository userRepository;

    /**
     * Encodeur de mots de passe (BCrypt) fourni par Spring Security.
     * Permet de crypter les mots de passe avant de les stocker en base.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * {@inheritDoc}
     *
     * Cette méthode :
     * 1. Vérifie que l'email n'est pas déjà utilisé
     * 2. Encode (crypte) le mot de passe avec BCrypt
     * 3. Sauvegarde l'utilisateur en base de données
     *
     * @param user l'utilisateur à créer (sans ID, avec mot de passe en clair)
     * @return l'utilisateur créé avec son ID généré et mot de passe crypté
     * @throws RuntimeException si l'email existe déjà
     */
    @Override
    public User createUser(User user) {
        // Vérification d'unicité de l'email
        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec l'email '" + user.getEmail() + "' existe déjà");
        }

        // Cryptage du mot de passe pour la sécurité
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Sauvegarde en base de données
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * Retourne la liste complète de tous les utilisateurs enregistrés.
     *
     * @return liste de tous les utilisateurs (peut être vide)
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     *
     * Recherche un utilisateur par son identifiant unique.
     * Utilise Optional pour gérer le cas où l'utilisateur n'existe pas.
     *
     * @param id l'identifiant technique de l'utilisateur
     * @return Optional contenant l'utilisateur ou vide si non trouvé
     */
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     *
     * Recherche un utilisateur par son adresse email (unique).
     * Utilisé notamment pour l'authentification et les vérifications d'unicité.
     *
     * @param email l'email exact de l'utilisateur
     * @return Optional contenant l'utilisateur ou vide si non trouvé
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * {@inheritDoc}
     *
     * Met à jour les informations d'un utilisateur existant.
     *
     * Étapes :
     * 1. Vérifier que l'utilisateur existe
     * 2. Vérifier que le nouvel email n'est pas déjà pris (si modifié)
     * 3. Mettre à jour les champs autorisés
     * 4. Sauvegarder les modifications
     *
     * Note : Le mot de passe n'est pas modifiable via cette méthode
     * pour des raisons de sécurité
     */
    @Override
    public User updateUser(Long id, User userDetails) {
        // 1. Vérifier que l'utilisateur existe
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));

        // 2. Vérifier l'unicité de l'email si celui-ci a changé
        if (!user.getEmail().equals(userDetails.getEmail()) &&
                existsByEmail(userDetails.getEmail())) {
            throw new RuntimeException("Un utilisateur avec l'email '" + userDetails.getEmail() + "' existe déjà");
        }

        // 3. Mise à jour des champs modifiables
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setRole(userDetails.getRole());

        // 4. Sauvegarde
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * Supprime un utilisateur de la base de données.
     *
     * @param id l'identifiant de l'utilisateur à supprimer
     * @throws RuntimeException si l'utilisateur n'existe pas
     */
    @Override
    public void deleteUser(Long id) {
        // Vérification que l'utilisateur existe avant suppression
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouvé avec l'id: " + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     *
     * Vérifie si une adresse email est déjà utilisée dans la base.
     * Utilisé lors de l'inscription et de la modification de profil.
     *
     * @param email l'email à vérifier
     * @return true si l'email existe déjà, false sinon
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}