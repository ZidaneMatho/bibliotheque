package com.example.bibliotheque.controllers;

import com.example.bibliotheque.models.Member;
import com.example.bibliotheque.services.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Membres", description = "API pour la gestion des membres de la bibliothèque")
public class MemberController {

    @Autowired
    private MemberService memberService;

    /**
     * Récupère tous les membres.
     * GET /api/members
     * @return liste de tous les membres
     */
    @Operation(summary = "Liste tous les membres",
            description = "Retourne la liste complète de tous les membres enregistrés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Non authentifié"),
            @ApiResponse(responseCode = "403", description = "Accès interdit (réservé aux administrateurs)")
    })
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
    @Operation(summary = "Recherche un membre par son ID",
            description = "Retourne un membre spécifique à partir de son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre trouvé"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(
            @Parameter(description = "ID du membre à rechercher", required = true)
            @PathVariable Long id) {
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
    @Operation(summary = "Recherche un membre par son numéro",
            description = "Retourne un membre spécifique à partir de son numéro unique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre trouvé"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé")
    })
    @GetMapping("/number/{memberNumber}")
    public ResponseEntity<Member> getMemberByNumber(
            @Parameter(description = "Numéro unique du membre à rechercher", required = true)
            @PathVariable String memberNumber) {
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
    @Operation(summary = "Crée un nouveau membre",
            description = "Ajoute un nouveau membre dans la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membre créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "409", description = "Numéro de membre déjà existant")
    })
    @PostMapping
    public ResponseEntity<Member> createMember(
            @Parameter(description = "Données du membre à créer", required = true)
            @RequestBody Member member) {
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
    @Operation(summary = "Met à jour un membre",
            description = "Modifie les informations d'un membre existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membre mis à jour"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @Parameter(description = "ID du membre à modifier", required = true)
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données du membre", required = true)
            @RequestBody Member member) {
        Member updatedMember = memberService.updateMember(id, member);
        return new ResponseEntity<>(updatedMember, HttpStatus.OK);
    }

    /**
     * Supprime un membre.
     * DELETE /api/members/{id}
     * @param id l'identifiant du membre à supprimer
     * @return réponse vide avec statut NO_CONTENT
     */
    @Operation(summary = "Supprime un membre",
            description = "Supprime définitivement un membre de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membre supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Membre non trouvé")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @Parameter(description = "ID du membre à supprimer", required = true)
            @PathVariable Long id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Vérifie si un numéro de membre existe déjà.
     * GET /api/members/exists/number/{memberNumber}
     * @param memberNumber le numéro à vérifier
     * @return true si le numéro existe, false sinon
     */
    @Operation(summary = "Vérifie si un numéro de membre existe",
            description = "Permet de savoir si un numéro de membre est déjà utilisé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vérification effectuée"),
            @ApiResponse(responseCode = "401", description = "Non authentifié")
    })
    @GetMapping("/exists/number/{memberNumber}")
    public ResponseEntity<Boolean> checkMemberNumberExists(
            @Parameter(description = "Numéro de membre à vérifier", required = true)
            @PathVariable String memberNumber) {
        boolean exists = memberService.existsByMemberNumber(memberNumber);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    /**
     * Récupère les membres avec des pénalités.
     * GET /api/members/with-penalties
     * @return liste des membres ayant des pénalités
     */
    @Operation(summary = "Liste les membres avec pénalités",
            description = "Retourne la liste des membres ayant des pénalités de retard (à implémenter)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "501", description = "Fonctionnalité non implémentée")
    })
    @GetMapping("/with-penalties")
    public ResponseEntity<List<Member>> getMembersWithPenalties() {
        // Cette méthode nécessite une méthode dans MemberService
        // À implémenter si nécessaire
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}