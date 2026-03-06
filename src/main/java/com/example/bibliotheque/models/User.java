package com.example.bibliotheque.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Classe représentant un utilisateur de l'application de gestion de bibliothèque.
 *
 * Cette classe est l'entité de base pour tous les types d'utilisateurs du système.
 * Elle contient les informations personnelles et d'authentification communes
 * à tous les utilisateurs, qu'ils soient membres ordinaires ou administrateurs.
 *
 * Les informations stockées comprennent l'identité de l'utilisateur (nom, prénom),
 * ses coordonnées (email, téléphone), ses données d'authentification (mot de passe),
 * ainsi que des métadonnées comme sa date d'inscription et son rôle dans l'application.
 *
 * @see Member
 * @see Administrator
 * @see Role
 *
 * @author  Groupe 5
 * @version 1.0
 * @since Mars 2026
 */
@Entity
@Table(name = "users")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class User {


    /**
     * Identifiant unique de l'utilisateur.
     *
     * Il s'agit de la clé primaire de la table. Sa valeur est générée
     * automatiquement par la base de données lors de l'insertion.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Prénom de l'utilisateur.
     *
     * Ce champ est obligatoire (ne peut pas être null en base de données).
     */
    @Column(name = "first_name", nullable = false)
    private String firstName;

    /**
     * Nom de famille de l'utilisateur.
     *
     * Ce champ est obligatoire (ne peut pas être null en base de données).
     */
    @Column(name = "last_name", nullable = false)
    private String lastName;

    /**
     * Adresse email de l'utilisateur.
     *
     * L'email est utilisé comme identifiant de connexion. Il doit être unique
     * dans toute la base de données et ne peut pas être null. Une contrainte
     * d'unicité est appliquée au niveau de la base de données.
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Mot de passe de l'utilisateur.
     *
     * Le mot de passe est stocké de manière sécurisée (hashé) en base de données.
     * Ce champ est obligatoire.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Numéro de téléphone de l'utilisateur.
     *
     * Ce champ est optionnel et peut-être null.
     */
    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Date et heure d'inscription de l'utilisateur.
     *
     * Cette valeur est renseignée automatiquement lors de la création du compte
     * et n'est pas modifiable par la suite (updatable = false).
     */
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    /**
     * Rôle de l'utilisateur dans l'application.
     *
     * Le rôle détermine les permissions et les fonctionnalités accessibles :
     * - ADMIN : accès à toutes les fonctionnalités de gestion
     * - MEMBER : accès limité au catalogue et à ses propres emprunts
     *
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}
