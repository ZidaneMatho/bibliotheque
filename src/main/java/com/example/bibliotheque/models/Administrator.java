package com.example.bibliotheque.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un administrateur (bibliothécaire).
 *
 * Un administrateur est un utilisateur avec des privilèges étendus
 * pour gérer la bibliothèque. Cette classe hérite de User.
 *
 * @see User
 * @author Équipe de développement
 * @version 1.0
 * @since Mars 2026
 */
@Entity
@Data
@Table(name = "administrators")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends User{
    /**
     * Niveau d'accès de l'administrateur.
     */
    @Column(name = "access_level")
    private String accessLevel;
    private String department;

    /**
     * Identifiant professionnel de l'administrateur.
     */
    @Column(name = "employee_id", unique = true)
    private String employeeId;
}
