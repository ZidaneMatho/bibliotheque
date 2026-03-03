package com.example.bibliotheque.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe représentant un livre dans le catalogue.
 *
 * Un livre est une ressource empruntable par les membres.
 * Il contient les informations bibliographiques et la gestion
 * du nombre d'exemplaires disponibles.
 *
 * @see Category
 * @see Borrow
 * @author Équipe de développement
 * @version 1.0
 * @since Mars 2026
 */
@Entity
@Data
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    /**
     * Identifiant unique du livre.
     * Généré automatiquement par la base de données.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ISBN (International Standard Book Number) du livre.
     */
    @Column(unique = true)
    private String isbn;

    /**
     * Titre complet du livre.
     * Champ obligatoire.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Nom de l'auteur du livre.
     * Champ obligatoire.
     */
    @Column(nullable = false)
    private String author;

    /**
     * Nom de l'éditeur du livre.
     */
    private String publisher;

    /**
     * Date de publication du livre.
     */
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    /**
     * Description ou résumé du livre.
     */
    private String description;

    /**
     * Nombre total d'exemplaires possédés par la bibliothèque.
     */
    @Column(name = "total_copies")
    private Integer totalCopies = 1;

    /**
     * Nombre d'exemplaires actuellement disponibles.
     */
    @Column(name = "available_copies")
    private Integer availableCopies = 1;

    /**
     * Date d'ajout du livre au catalogue.
     */
    @Column(name = "added_date")
    private LocalDateTime addedDate;
}
