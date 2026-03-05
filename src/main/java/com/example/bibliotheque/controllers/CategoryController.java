package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Category;
import com.example.bibliotheque.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des catégories.
 * Toutes les routes commencent par /api/categories
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Récupère toutes les catégories.
     * GET /api/categories
     * @return liste des catégories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Récupère une catégorie par son ID.
     * GET /api/categories/{id}
     * @param id l'identifiant de la catégorie
     * @return la catégorie trouvée
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'id: " + id));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Récupère une catégorie par son nom.
     * GET /api/categories/name/{name}
     * @param name le nom de la catégorie
     * @return la catégorie trouvée
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec le nom: " + name));
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * Crée une nouvelle catégorie.
     * POST /api/categories
     * @param category les données de la catégorie à créer
     * @return la catégorie créée
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        // Vérifier si le nom existe déjà
        if (categoryService.existsByName(category.getName())) {
            throw new RuntimeException("Une catégorie avec le nom '" + category.getName() + "' existe déjà");
        }
        Category newCategory = categoryService.createCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);
    }

    /**
     * Met à jour une catégorie existante.
     * PUT /api/categories/{id}
     * @param id l'identifiant de la catégorie à modifier
     * @param category les nouvelles données
     * @return la catégorie mise à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    /**
     * Supprime une catégorie.
     * DELETE /api/categories/{id}
     * @param id l'identifiant de la catégorie à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un nom de catégorie existe déjà.
     * GET /api/categories/exists/name/{name}
     * @param name le nom à vérifier
     * @return true si le nom existe, false sinon
     */
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> checkNameExists(@PathVariable String name) {
        boolean exists = categoryService.existsByName(name);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}
