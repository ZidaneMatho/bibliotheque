package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Administrator;
import java.util.List;
import java.util.Optional;

/**
 * Interface du service de gestion des administrateurs.
 * Définit les opérations disponibles pour les administrateurs de la bibliothèque.
 */
public interface AdministratorService {

    /**
     * Crée un nouvel administrateur.
     * @param admin les informations de l'administrateur à créer
     * @return l'administrateur créé avec son ID généré
     */
    Administrator createAdministrator(Administrator admin);

    /**
     * Récupère tous les administrateurs.
     * @return la liste de tous les administrateurs
     */
    List<Administrator> getAllAdministrators();

    /**
     * Recherche un administrateur par son ID.
     * @param id l'identifiant de l'administrateur
     * @return l'administrateur trouvé ou Optional vide
     */
    Optional<Administrator> getAdministratorById(Long id);

    /**
     * Recherche un administrateur par son matricule.
     * @param employeeId le matricule de l'administrateur
     * @return l'administrateur trouvé ou Optional vide
     */
    Optional<Administrator> getAdministratorByEmployeeId(String employeeId);


    /**
     * Met à jour un administrateur existant.
     * @param id l'identifiant de l'administrateur à modifier
     * @param adminDetails les nouvelles informations
     * @return l'administrateur mis à jour
     */
    Administrator updateAdministrator(Long id, Administrator adminDetails);

    /**
     * Supprime un administrateur.
     * @param id l'identifiant de l'administrateur à supprimer
     */
    void deleteAdministrator(Long id);

    /**
     * Vérifie si un matricule existe déjà.
     * @param employeeId le matricule à vérifier
     * @return true si le matricule existe déjà, false sinon
     */
    boolean existsByEmployeeId(String employeeId);
}