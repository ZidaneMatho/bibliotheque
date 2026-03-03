package com.example.bibliotheque.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

/**
 * Classe représentant une catégorie de livres.
 *
 * Permet de classer les livres par genres ou thèmes
 * pour faciliter la recherche dans le catalogue.
 *
 * @see Book
 * @author Équipe de développement
 * @version 1.0
 * @since Mars 2026
 */
@Entity
@Data
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String name;
    private  String description;
}
