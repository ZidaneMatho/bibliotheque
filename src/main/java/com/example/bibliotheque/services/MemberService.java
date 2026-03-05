package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Member;
import java.util.List;
import java.util.Optional;

/**
 * Interface du service de gestion des membres.
 * Définit les opérations spécifiques aux membres de la bibliothèque.
 */
public interface MemberService {

    /**
     * Crée un nouveau membre.
     * @param member les informations du membre à créer
     * @return le membre créé avec son ID généré
     */
    Member createMember(Member member);

    /**
     * Récupère tous les membres.
     * @return la liste de tous les membres
     */
    List<Member> getAllMembers();

    /**
     * Recherche un membre par son ID.
     * @param id l'identifiant du membre
     * @return le membre trouvé ou Optional vide
     */
    Optional<Member> getMemberById(Long id);

    /**
     * Recherche un membre par son numéro unique.
     * @param memberNumber le numéro du membre
     * @return le membre trouvé ou Optional vide
     */
    Optional<Member> getMemberByNumber(String memberNumber);


    /**
     * Met à jour un membre existant.
     * @param id l'identifiant du membre à modifier
     * @param memberDetails les nouvelles informations
     * @return le membre mis à jour
     */
    Member updateMember(Long id, Member memberDetails);

    /**
     * Supprime un membre.
     * @param id l'identifiant du membre à supprimer
     */
    void deleteMember(Long id);

    /**
     * Vérifie si un numéro de membre existe déjà.
     * @param memberNumber le numéro à vérifier
     * @return true si le numéro existe déjà, false sinon
     */
    boolean existsByMemberNumber(String memberNumber);
}