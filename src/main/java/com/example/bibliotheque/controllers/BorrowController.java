package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Borrow;
import com.example.bibliotheque.models.BorrowStatus;
import com.example.bibliotheque.services.BorrowService;
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
 * Contrôleur REST pour la gestion des emprunts.
 * Toutes les routes commencent par /api/borrows
 */
@RestController
@RequestMapping("/api/borrows")
@Tag(name = "Emprunts", description = "API pour la gestion des emprunts de livres")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    /**
     * Récupère tous les emprunts.
     * GET /api/borrows
     */
    @Operation(summary = "Liste tous les emprunts",
            description = "Retourne la liste complète de tous les emprunts (réservé aux administrateurs)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)")
    })
    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        List<Borrow> borrows = borrowService.getAllBorrows();
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère un emprunt par son ID.
     * GET /api/borrows/{id}
     */
    @Operation(summary = "Recherche un emprunt par son ID",
            description = "Retourne un emprunt spécifique à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emprunt trouvé"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(
            @Parameter(description = "ID de l'emprunt à rechercher", required = true)
            @PathVariable Long id) {
        Borrow borrow = borrowService.getBorrowById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id: " + id));
        return new ResponseEntity<>(borrow, HttpStatus.OK);
    }

    /**
     * Récupère tous les emprunts d'un membre.
     * GET /api/borrows/member/{memberId}
     */
    @Operation(summary = "Liste les emprunts d'un membre",
            description = "Retourne tous les emprunts effectués par un membre spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Borrow>> getBorrowsByMemberId(
            @Parameter(description = "ID du membre", required = true)
            @PathVariable Long memberId) {
        List<Borrow> borrows = borrowService.getBorrowsByMemberId(memberId);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère tous les emprunts d'un livre.
     * GET /api/borrows/book/{bookId}
     */
    @Operation(summary = "Liste les emprunts d'un livre",
            description = "Retourne tous les emprunts d'un livre spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Borrow>> getBorrowsByBookId(
            @Parameter(description = "ID du livre", required = true)
            @PathVariable Long bookId) {
        List<Borrow> borrows = borrowService.getBorrowsByBookId(bookId);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère les emprunts par statut.
     * GET /api/borrows/status/{status}
     */
    @Operation(summary = "Liste les emprunts par statut",
            description = "Retourne tous les emprunts ayant un statut spécifique (ACTIVE, RETURNED, LATE, etc.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Borrow>> getBorrowsByStatus(
            @Parameter(description = "Statut des emprunts (ACTIVE, EXTENDED, RETURNED, LATE)", required = true)
            @PathVariable BorrowStatus status) {
        List<Borrow> borrows = borrowService.getBorrowsByStatus(status);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère les emprunts actifs d'un membre.
     * GET /api/borrows/member/{memberId}/active
     */
    @Operation(summary = "Liste les emprunts actifs d'un membre",
            description = "Retourne tous les emprunts en cours d'un membre spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<Borrow>> getActiveBorrowsByMemberId(
            @Parameter(description = "ID du membre", required = true)
            @PathVariable Long memberId) {
        List<Borrow> borrows = borrowService.getActiveBorrowsByMemberId(memberId);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Crée un nouvel emprunt.
     * POST /api/borrows
     */
    @Operation(summary = "Crée un nouvel emprunt",
            description = "Permet à un membre d'emprunter un livre (vérifie la disponibilité et la limite d'emprunts)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Emprunt créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Membre ou livre non trouvé"),
            @ApiResponse(responseCode = "409", description = "Livre non disponible ou limite d'emprunts atteinte")
    })
    @PostMapping
    public ResponseEntity<Borrow> createBorrow(
            @Parameter(description = "Données de l'emprunt à créer (membre et livre)", required = true)
            @RequestBody Borrow borrow) {
        Borrow newBorrow = borrowService.createBorrow(borrow);
        return new ResponseEntity<>(newBorrow, HttpStatus.CREATED);
    }

    /**
     * Met à jour un emprunt.
     * PUT /api/borrows/{id}
     */
    @Operation(summary = "Met à jour un emprunt",
            description = "Modifie les informations d'un emprunt existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emprunt mis à jour"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Borrow> updateBorrow(
            @Parameter(description = "ID de l'emprunt à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données de l'emprunt", required = true)
            @RequestBody Borrow borrow) {
        Borrow updatedBorrow = borrowService.updateBorrow(id, borrow);
        return new ResponseEntity<>(updatedBorrow, HttpStatus.OK);
    }

    /**
     * Supprime un emprunt.
     * DELETE /api/borrows/{id}
     */
    @Operation(summary = "Supprime un emprunt",
            description = "Supprime définitivement un emprunt de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Emprunt supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(
            @Parameter(description = "ID de l'emprunt à supprimer", required = true)
            @PathVariable Long id) {
        borrowService.deleteBorrow(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retourne un livre emprunté.
     * PUT /api/borrows/{id}/return
     */
    @Operation(summary = "Retourne un livre",
            description = "Enregistre le retour d'un livre et calcule les pénalités si nécessaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livre retourné avec succès"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé"),
            @ApiResponse(responseCode = "409", description = "L'emprunt n'est pas actif")
    })
    @PutMapping("/{id}/return")
    public ResponseEntity<Borrow> returnBook(
            @Parameter(description = "ID de l'emprunt à retourner", required = true)
            @PathVariable Long id) {
        Borrow borrow = borrowService.returnBook(id);
        return new ResponseEntity<>(borrow, HttpStatus.OK);
    }

    /**
     * Prolonge un emprunt.
     * PUT /api/borrows/{id}/extend?days=7
     */
    @Operation(summary = "Prolonge un emprunt",
            description = "Prolonge la durée d'un emprunt actif (nombre de jours en paramètre)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emprunt prolongé avec succès"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé"),
            @ApiResponse(responseCode = "409", description = "L'emprunt n'est pas actif")
    })
    @PutMapping("/{id}/extend")
    public ResponseEntity<Borrow> extendBorrow(
            @Parameter(description = "ID de l'emprunt à prolonger", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nombre de jours de prolongation", required = true, example = "7")
            @RequestParam int days) {
        Borrow borrow = borrowService.extendBorrow(id, days);
        return new ResponseEntity<>(borrow, HttpStatus.OK);
    }

    /**
     * Calcule la pénalité d'un emprunt.
     * GET /api/borrows/{id}/penalty
     */
    @Operation(summary = "Calcule la pénalité",
            description = "Calcule le montant de la pénalité pour un emprunt (100 FCFA par jour de retard)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pénalité calculée avec succès"),
            @ApiResponse(responseCode = "404", description = "Emprunt non trouvé")
    })
    @GetMapping("/{id}/penalty")
    public ResponseEntity<Double> calculatePenalty(
            @Parameter(description = "ID de l'emprunt", required = true)
            @PathVariable Long id) {
        double penalty = borrowService.calculatePenalty(id);
        return new ResponseEntity<>(penalty, HttpStatus.OK);
    }
}