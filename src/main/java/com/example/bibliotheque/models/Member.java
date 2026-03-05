package com.example.bibliotheque.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un membre de la bibliothèque.
 *
 * Un membre est un utilisateur qui peut emprunter des livres.
 * Cette classe contient des attributs spécifiques aux membres
 * comme le numéro de membre, l'adresse et les pénalités.
 *
 * @see User
 * @see Borrow
 * @author Groupe 5
 * @version 1.0
 * @since Mars 2026
 */
@Entity
@Table(name = "members")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends User {
    /**
     * Numéro unique identifiant le membre dans la bibliothèque.
     */
    @Column(name = "member_number",unique = true,nullable = false)
    private String memberNumber;
    /**
     * Montant total des pénalités accumulées.
     */
    private Double penalties=0.0;
    private  String address;
    @Column(name = "birth_date")
    private LocalDate birthdate;
    /**
     * Liste des emprunts effectués par ce membre.
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Borrow> borrows = new ArrayList<>();

}
