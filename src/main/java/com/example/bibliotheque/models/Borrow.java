package com.example.bibliotheque.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Classe représentant un emprunt de livre.
 *
 * Cette classe gère la relation entre un membre et un livre,
 * avec les dates d'emprunt et de retour, ainsi que les pénalités.
 *
 * @see Member
 * @see Book
 * @see BorrowStatus
 * @author Équipe de développement
 * @version 1.0
 * @since Mars 2026
 */
@Entity
@Table(name = "borrows")
@Data
@NoArgsConstructor
public class Borrow {

    /**
     * Identifiant unique de l'emprunt.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Membre ayant effectué l'emprunt.
     */
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * Livre emprunté.
     */
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    /**
     * Date et heure de l'emprunt.
     */
    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;

    /**
     * Date de retour prévue (14 jours après l'emprunt par défaut).
     */
    @Column(name = "expected_return_date")
    private LocalDateTime expectedReturnDate;

    /**
     * Date de retour réelle (null si pas encore retourné).
     */
    @Column(name = "actual_return_date")
    private LocalDateTime actualReturnDate;

    /**
     * Statut actuel de l'emprunt.
     *
     * @see BorrowStatus
     */
    @Enumerated(EnumType.STRING)
    private BorrowStatus status;

    /**
     * Montant de la pénalité en cas de retard.
     */
    private Double penalty = 0.0;
}