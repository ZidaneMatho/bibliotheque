package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Category;
import com.example.bibliotheque.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des catégories.
 * Contient la logique métier pour les opérations sur les catégories.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    /** Repository pour accéder à la base de données des catégories. */
    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     *
     * Sauvegarde une nouvelle catégorie.
     * Le nom doit être unique.
     */
    @Override
    public Category createCategory(Category category) {
        // Vérifier si le nom existe déjà
        if (categoryRepository.findByName(category.getName()).isPresent()) {
            throw new RuntimeException("Une catégorie avec le nom '" + category.getName() + "' existe déjà");
        }
        return categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    /**
     * {@inheritDoc}
     *
     * Met à jour les informations d'une catégorie existante.
     * @throws RuntimeException si la catégorie n'existe pas
     */
    @Override
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));

        // Vérifier si le nouveau nom est déjà pris par une autre catégorie
        if (!category.getName().equals(categoryDetails.getName())) {
            categoryRepository.findByName(categoryDetails.getName())
                    .ifPresent(c -> {
                        throw new RuntimeException("Une catégorie avec le nom '" + categoryDetails.getName() + "' existe déjà");
                    });
        }

        // Mise à jour des champs
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        return categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     *
     * Supprime une catégorie si elle n'est pas utilisée par des livres.
     * @throws RuntimeException si la catégorie est associée à des livres
     */
    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));

        // Vérifier si la catégorie est utilisée
        if (!category.getBooks().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer cette catégorie car elle est associée à "
                    + category.getBooks().size() + " livre(s)");
        }

        categoryRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsByName(String name) {
        return categoryRepository.findByName(name).isPresent();
    }
}