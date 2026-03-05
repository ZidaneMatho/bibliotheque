package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Borrow;
import com.example.bibliotheque.models.BorrowStatus;
import com.example.bibliotheque.models.Book;
import com.example.bibliotheque.models.Member;
import com.example.bibliotheque.repository.BorrowRepository;
import com.example.bibliotheque.repository.BookRepository;
import com.example.bibliotheque.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des emprunts.
 * Contient la logique métier pour les opérations d'emprunt,
 * de retour, de prolongation et de calcul des pénalités.
 */
@Service
public class BorrowServiceImpl implements BorrowService {

    /** Repository pour les opérations sur les emprunts. */
    @Autowired
    private BorrowRepository borrowRepository;

    /** Repository pour les opérations sur les livres. */
    @Autowired
    private BookRepository bookRepository;

    /** Repository pour les opérations sur les membres. */
    @Autowired
    private MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     *
     * Cette méthode effectue plusieurs vérifications avant la création :
     * - Le livre doit exister et être disponible
     * - Le membre doit exister
     * - Le membre ne doit pas avoir plus de 5 emprunts actifs.
     *
     * Elle initialise également les dates et met à jour la disponibilité du livre.
     */
    @Override
    @Transactional
    public Borrow createBorrow(Borrow borrow) {
        // Vérifier que le livre existe et est disponible
        Book book = bookRepository.findById(borrow.getBook().getId())
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + borrow.getBook().getId()));

        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException("Le livre '" + book.getTitle() + "' n'est pas disponible actuellement");
        }

        // Vérifier que le membre existe
        Member member = memberRepository.findById(borrow.getMember().getId())
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'ID: " + borrow.getMember().getId()));

        // Vérifier que le membre n'a pas trop d'emprunts en cours (limite = 5)
        long activeBorrows = borrowRepository.countByMemberIdAndStatus(
                member.getId(), BorrowStatus.ACTIVE);

        if (activeBorrows >= 5) {
            throw new RuntimeException("Le membre a déjà 5 emprunts en cours (maximum autorisé)");
        }

        // Initialiser les données de l'emprunt
        borrow.setBorrowDate(LocalDateTime.now());
        borrow.setExpectedReturnDate(LocalDateTime.now().plusDays(14)); // 14 jours par défaut
        borrow.setStatus(BorrowStatus.ACTIVE);
        borrow.setPenalty(0.0);

        // Mettre à jour la disponibilité du livre (diminuer le nombre d'exemplaires disponibles)
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        // Sauvegarder et retourner l'emprunt
        return borrowRepository.save(borrow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Borrow> getAllBorrows() {
        return borrowRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Borrow> getBorrowById(Long id) {
        return borrowRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Borrow> getBorrowsByMemberId(Long memberId) {
        return borrowRepository.findByMemberId(memberId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Borrow> getBorrowsByBookId(Long bookId) {
        return borrowRepository.findByBookId(bookId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Borrow> getBorrowsByStatus(BorrowStatus status) {
        return borrowRepository.findByStatus(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Borrow> getActiveBorrowsByMemberId(Long memberId) {
        return borrowRepository.findByMemberIdAndStatus(memberId, BorrowStatus.ACTIVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Borrow updateBorrow(Long id, Borrow borrowDetails) {
        Borrow borrow = borrowRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id: " + id));

        // Mise à jour des champs modifiables
        borrow.setActualReturnDate(borrowDetails.getActualReturnDate());
        borrow.setStatus(borrowDetails.getStatus());
        borrow.setPenalty(borrowDetails.getPenalty());

        return borrowRepository.save(borrow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBorrow(Long id) {
        if (!borrowRepository.existsById(id)) {
            throw new RuntimeException("Emprunt non trouvé avec l'id: " + id);
        }
        borrowRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     *
     * Cette méthode gère le retour d'un livre :
     * Enregistre la date de retour réelle
     * Calcule les pénalités si le retour est en retard
     * Met à jour le statut de l'emprunt (RETURNED ou LATE)
     * Remet le livre disponible (augmente le nombre d'exemplaires)
     */
    @Override
    @Transactional
    public Borrow returnBook(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id: " + borrowId));

        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new RuntimeException("Cet emprunt n'est pas actif (statut actuel: " + borrow.getStatus() + ")");
        }

        // Enregistrer la date de retour
        borrow.setActualReturnDate(LocalDateTime.now());

        // Calculer les pénalités si retard
        if (borrow.getActualReturnDate().isAfter(borrow.getExpectedReturnDate())) {
            long daysLate = ChronoUnit.DAYS.between(
                    borrow.getExpectedReturnDate(), borrow.getActualReturnDate());
            borrow.setPenalty((double) (daysLate * 100)); // 100 FCFA par jour de retard
            borrow.setStatus(BorrowStatus.LATE);
        } else {
            borrow.setStatus(BorrowStatus.RETURNED);
        }

        // Remettre le livre disponible
        Book book = borrow.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return borrowRepository.save(borrow);
    }

    /**
     * {@inheritDoc}
     *
     * Prolonge la durée d'un emprunt actif.
     * Le statut passe à ACTIVE à EXTENDED.
     */
    @Override
    @Transactional
    public Borrow extendBorrow(Long borrowId, int days) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id: " + borrowId));

        if (borrow.getStatus() != BorrowStatus.ACTIVE) {
            throw new RuntimeException("Impossible de prolonger un emprunt non actif (statut: " + borrow.getStatus() + ")");
        }

        // Prolonger la date de retour
        borrow.setExpectedReturnDate(borrow.getExpectedReturnDate().plusDays(days));
        borrow.setStatus(BorrowStatus.EXTENDED);

        return borrowRepository.save(borrow);
    }

    /**
     * {@inheritDoc}
     *
     * Calcule les pénalités pour un emprunt
     * Si le livre n'est pas encore retourné : calcule avec la date actuelle
     * Si le livre est retourné : utilise la date de retour réelle
     * Tarif : 100 FCFA par jour de retard
     */
    @Override
    public double calculatePenalty(Long borrowId) {
        Borrow borrow = borrowRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'id: " + borrowId));

        if (borrow.getActualReturnDate() == null) {
            // Cas 1 : le livre pas encore retourné
            if (LocalDateTime.now().isAfter(borrow.getExpectedReturnDate())) {
                long daysLate = ChronoUnit.DAYS.between(
                        borrow.getExpectedReturnDate(), LocalDateTime.now());
                return daysLate * 100;
            }
            return 0;
        } else {
            // Cas 2 : Livre déjà retourné
            if (borrow.getActualReturnDate().isAfter(borrow.getExpectedReturnDate())) {
                long daysLate = ChronoUnit.DAYS.between(
                        borrow.getExpectedReturnDate(), borrow.getActualReturnDate());
                return daysLate * 100;
            }
            return borrow.getPenalty();
        }
    }
}