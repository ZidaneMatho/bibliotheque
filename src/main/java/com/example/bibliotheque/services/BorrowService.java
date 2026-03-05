package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Borrow;
import com.example.bibliotheque.models.BorrowStatus;
import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des emprunts.
 */
public interface BorrowService {

    /** Crée un nouvel emprunt. */
    Borrow createBorrow(Borrow borrow);

    /** Retourne tous les emprunts. */
    List<Borrow> getAllBorrows();

    /** Recherche un emprunt par son ID. */
    Optional<Borrow> getBorrowById(Long id);

    /** Retourne les emprunts d'un membre. */
    List<Borrow> getBorrowsByMemberId(Long memberId);

    /** Retourne les emprunts d'un livre. */
    List<Borrow> getBorrowsByBookId(Long bookId);

    /** Retourne les emprunts par statut. */
    List<Borrow> getBorrowsByStatus(BorrowStatus status);

    /** Retourne les emprunts en cours d'un membre. */
    List<Borrow> getActiveBorrowsByMemberId(Long memberId);

    /** Met à jour un emprunt existant. */
    Borrow updateBorrow(Long id, Borrow borrowDetails);

    /** Supprime un emprunt. */
    void deleteBorrow(Long id);

    /** Enregistre le retour d'un livre. */
    Borrow returnBook(Long borrowId);

    /** Prolonge un emprunt. */
    Borrow extendBorrow(Long borrowId, int days);

    /** Calcule les pénalités d'un emprunt. */
    double calculatePenalty(Long borrowId);
}