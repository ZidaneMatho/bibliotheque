package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Borrow;
import com.example.bibliotheque.models.BorrowStatus;
import com.example.bibliotheque.services.BorrowService;
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
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    /**
     * Récupère tous les emprunts.
     * GET /api/borrows
     */
    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        List<Borrow> borrows = borrowService.getAllBorrows();
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère un emprunt par son ID.
     * GET /api/borrows/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(@PathVariable Long id) {
        Borrow borrow = borrowService.getBorrowById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id: " + id));
        return new ResponseEntity<>(borrow, HttpStatus.OK);
    }

    /**
     * Récupère tous les emprunts d'un membre.
     * GET /api/borrows/member/{memberId}
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Borrow>> getBorrowsByMemberId(@PathVariable Long memberId) {
        List<Borrow> borrows = borrowService.getBorrowsByMemberId(memberId);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère tous les emprunts d'un livre.
     * GET /api/borrows/book/{bookId}
     */
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Borrow>> getBorrowsByBookId(@PathVariable Long bookId) {
        List<Borrow> borrows = borrowService.getBorrowsByBookId(bookId);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère les emprunts par statut.
     * GET /api/borrows/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Borrow>> getBorrowsByStatus(@PathVariable BorrowStatus status) {
        List<Borrow> borrows = borrowService.getBorrowsByStatus(status);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Récupère les emprunts actifs d'un membre.
     * GET /api/borrows/member/{memberId}/active
     */
    @GetMapping("/member/{memberId}/active")
    public ResponseEntity<List<Borrow>> getActiveBorrowsByMemberId(@PathVariable Long memberId) {
        List<Borrow> borrows = borrowService.getActiveBorrowsByMemberId(memberId);
        return new ResponseEntity<>(borrows, HttpStatus.OK);
    }

    /**
     * Crée un nouvel emprunt.
     * POST /api/borrows
     */
    @PostMapping
    public ResponseEntity<Borrow> createBorrow(@RequestBody Borrow borrow) {
        Borrow newBorrow = borrowService.createBorrow(borrow);
        return new ResponseEntity<>(newBorrow, HttpStatus.CREATED);
    }

    /**
     * Met à jour un emprunt.
     * PUT /api/borrows/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Borrow> updateBorrow(@PathVariable Long id, @RequestBody Borrow borrow) {
        Borrow updatedBorrow = borrowService.updateBorrow(id, borrow);
        return new ResponseEntity<>(updatedBorrow, HttpStatus.OK);
    }

    /**
     * Supprime un emprunt.
     * DELETE /api/borrows/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(@PathVariable Long id) {
        borrowService.deleteBorrow(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retourne un livre emprunté.
     * PUT /api/borrows/{id}/return
     */
    @PutMapping("/{id}/return")
    public ResponseEntity<Borrow> returnBook(@PathVariable Long id) {
        Borrow borrow = borrowService.returnBook(id);
        return new ResponseEntity<>(borrow, HttpStatus.OK);
    }

    /**
     * Prolonge un emprunt.
     * PUT /api/borrows/{id}/extend?days=7
     */
    @PutMapping("/{id}/extend")
    public ResponseEntity<Borrow> extendBorrow(@PathVariable Long id, @RequestParam int days) {
        Borrow borrow = borrowService.extendBorrow(id, days);
        return new ResponseEntity<>(borrow, HttpStatus.OK);
    }

    /**
     * Calcule la pénalité d'un emprunt.
     * GET /api/borrows/{id}/penalty
     */
    @GetMapping("/{id}/penalty")
    public ResponseEntity<Double> calculatePenalty(@PathVariable Long id) {
        double penalty = borrowService.calculatePenalty(id);
        return new ResponseEntity<>(penalty, HttpStatus.OK);
    }
}