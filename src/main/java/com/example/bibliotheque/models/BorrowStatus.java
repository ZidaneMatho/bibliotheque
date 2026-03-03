package com.example.bibliotheque.models;
/**
 * Énumération représentant les différents statuts d'un emprunt.
 *
 * Cette énumération permet de suivre l'état d'un emprunt tout au long
 * de son cycle de vie, depuis sa création jusqu'à son retour.
 *
 * @author Groupe 5
 * @version 1.0
 * @since Mars 2026
 */
public enum BorrowStatus {
    /**
     * Emprunt en cours : le livre n'a pas encore été retourné.
     */
    ACTIVE,

    /**
     * Emprunt prolongé : la date de retour a été repoussée.
     */
    EXTENDED,

    /**
     * Emprunt retourné : le livre a été rendu dans les délais.
     */
    RETURNED,

    /**
     * Emprunt en retard : le livre a été rendu après la date prévue.
     * Une pénalité sera appliquée.
     */
    LATE

}
