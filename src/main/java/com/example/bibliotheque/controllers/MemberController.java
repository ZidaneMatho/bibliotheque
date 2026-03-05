package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Member;
import com.example.bibliotheque.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des membres.
 * Toutes les routes commencent par /api/members
 */
@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * Récupère tous les membres.
     * GET /api/members
     * @return liste de tous les membres
     */
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    /**
     * Récupère un membre par son ID.
     * GET /api/members/{id}
     * @param id l'identifiant du membre
     * @return le membre trouvé
     */
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'id: " + id));
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * Récupère un membre par son numéro de membre.
     * GET /api/members/number/{memberNumber}
     * @param memberNumber le numéro unique du membre
     * @return le membre trouvé
     */
    @GetMapping("/number/{memberNumber}")
    public ResponseEntity<Member> getMemberByNumber(@PathVariable String memberNumber) {
        Member member = memberService.getMemberByNumber(memberNumber)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec le numéro: " + memberNumber));
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    /**
     * Crée un nouveau membre.
     * POST /api/members
     * @param member les informations du membre à créer
     * @return le membre créé
     */
    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        // Vérifier si le numéro de membre existe déjà
        if (member.getMemberNumber() != null &&
                memberService.existsByMemberNumber(member.getMemberNumber())) {
            throw new RuntimeException("Un membre avec le numéro '" + member.getMemberNumber() + "' existe déjà");
        }
        Member newMember = memberService.createMember(member);
        return new ResponseEntity<>(newMember, HttpStatus.CREATED);
    }

    /**
     * Met à jour un membre existant.
     * PUT /api/members/{id}
     * @param id l'identifiant du membre à modifier
     * @param member les nouvelles informations
     * @return le membre mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member member) {
        Member updatedMember = memberService.updateMember(id, member);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    /**
     * Supprime un membre.
     * DELETE /api/members/{id}
     * @param id l'identifiant du membre à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un numéro de membre existe déjà.
     * GET /api/members/exists/number/{memberNumber}
     * @param memberNumber le numéro à vérifier
     * @return true si le numéro existe, false sinon
     */
    @GetMapping("/exists/number/{memberNumber}")
    public ResponseEntity<Boolean> checkMemberNumberExists(@PathVariable String memberNumber) {
        boolean exists = memberService.existsByMemberNumber(memberNumber);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    /**
     * Récupère les membres avec des pénalités.
     * GET /api/members/with-penalties
     * @return liste des membres ayant des pénalités
     */
    @GetMapping("/with-penalties")
    public ResponseEntity<List<Member>> getMembersWithPenalties() {
        // Cette méthode nécessite une méthode dans MemberService
        // À implémenter si nécessaire
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}