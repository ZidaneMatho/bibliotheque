package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Category;
import com.example.bibliotheque.services.CategoryService;
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
 * Contrôleur REST pour la gestion des catégories.
 * Toutes les routes commencent par /api/categories
 */
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Catégories", description = "API pour la gestion des catégories de livres")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * Récupère toutes les catégories.
     * GET /api/categories
     * @return liste des catégories
     */
    @Operation(summary = "Liste toutes les catégories",
            description = "Retourne la liste complète de toutes les catégories de livres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)")
    })
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
    @Operation(summary = "Recherche une catégorie par son ID",
            description = "Retourne une catégorie spécifique à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @Parameter(description = "ID de la catégorie à rechercher", required = true)
            @PathVariable Long id) {
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
    @Operation(summary = "Recherche une catégorie par son nom",
            description = "Retourne une catégorie spécifique à partir de son nom unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie trouvée"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(
            @Parameter(description = "Nom de la catégorie à rechercher", required = true)
            @PathVariable String name) {
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
    @Operation(summary = "Crée une nouvelle catégorie",
            description = "Ajoute une nouvelle catégorie de livres dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Catégorie créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Nom de catégorie déjà existant")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Parameter(description = "Données de la catégorie à créer", required = true)
            @RequestBody Category category) {
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
    @Operation(summary = "Met à jour une catégorie",
            description = "Modifie les informations d'une catégorie existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Catégorie mise à jour"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "409", description = "Nom déjà utilisé par une autre catégorie")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @Parameter(description = "ID de la catégorie à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de la catégorie", required = true)
            @RequestBody Category category) {
        Category updatedCategory = categoryService.updateCategory(id, category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
    }

    /**
     * Supprime une catégorie.
     * DELETE /api/categories/{id}
     * @param id l'identifiant de la catégorie à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @Operation(summary = "Supprime une catégorie",
            description = "Supprime définitivement une catégorie de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Catégorie supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Catégorie non trouvée"),
            @ApiResponse(responseCode = "409", description = "Catégorie utilisée par des livres, suppression impossible")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID de la catégorie à supprimer", required = true)
            @PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un nom de catégorie existe déjà.
     * GET /api/categories/exists/name/{name}
     * @param name le nom à vérifier
     * @return true si le nom existe, false sinon
     */
    @Operation(summary = "Vérifie si un nom de catégorie existe",
            description = "Permet de savoir si un nom de catégorie est déjà utilisé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/exists/name/{name}")
    public ResponseEntity<Boolean> checkNameExists(
            @Parameter(description = "Nom de catégorie à vérifier", required = true)
            @PathVariable String name) {
        boolean exists = categoryService.existsByName(name);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
}