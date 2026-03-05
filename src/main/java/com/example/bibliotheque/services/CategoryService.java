package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Category;
import java.util.List;
import java.util.Optional;

/**
 * Interface du service de gestion des catégories.
 * Définit les opérations disponibles pour les catégories de livres.
 */
public interface CategoryService {

    /**
     * Crée une nouvelle catégorie.
     * @param category les informations de la catégorie à créer
     * @return la catégorie créée avec son ID généré
     */
    Category createCategory(Category category);

    /**
     * Récupère toutes les catégories.
     * @return la liste de toutes les catégories
     */
    List<Category> getAllCategories();

    /**
     * Recherche une catégorie par son ID.
     * @param id l'identifiant de la catégorie
     * @return la catégorie trouvée ou Optional vide
     */
    Optional<Category> getCategoryById(Long id);

    /**
     * Recherche une catégorie par son nom.
     * @param name le nom de la catégorie
     * @return la catégorie trouvée ou Optional vide
     */
    Optional<Category> getCategoryByName(String name);

    /**
     * Met à jour une catégorie existante.
     * @param id l'identifiant de la catégorie à modifier
     * @param categoryDetails les nouvelles informations
     * @return la catégorie mise à jour
     */
    Category updateCategory(Long id, Category categoryDetails);

    /**
     * Supprime une catégorie.
     * @param id l'identifiant de la catégorie à supprimer
     */
    void deleteCategory(Long id);

    /**
     * Vérifie si un nom de catégorie existe déjà.
     * @param name le nom à vérifier
     * @return true si le nom existe déjà, false sinon
     */
    boolean existsByName(String name);
}