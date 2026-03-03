package com.example.bibliotheque.models;

/**
 * Énumération des rôles des utilisateurs dans l'application.
 *
 * Elle est utilisée dans la classe User pour gérer les permissions.
 *
 * @author Groupe 5
 * @version 1.0
 * @since Mars 2026
 */
public enum Role {

    /**
     * Rôle administrateur (bibliothécaire).
     * Peut gérer les livres, les membres et voir les statistiques.
     */
    ADMIN,

    /**
     * Rôle membre ordinaire.
     * Peut consulter le catalogue et gérer ses propres emprunts.
     */
    MEMBER
}