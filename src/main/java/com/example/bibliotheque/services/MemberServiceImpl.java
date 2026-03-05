package com.example.bibliotheque.services;

import com.example.bibliotheque.models.Member;
import com.example.bibliotheque.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du service de gestion des membres.
 * Contient la logique métier pour les opérations sur les membres.
 */
@Service
public class MemberServiceImpl implements MemberService {

    /**
     * Repository pour accéder à la base de données des membres.
     */
    @Autowired
    private MemberRepository memberRepository;

    /**
     * {@inheritDoc}
     * Sauvegarde un nouveau membre dans la base de données.
     */
    @Override
    public Member createMember(Member member) {
        return memberRepository.save(member);
    }

    /**
     * {@inheritDoc}
     * Retourne tous les membres de la base.
     */
    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    /**
     * {@inheritDoc}
     * Recherche un membre par son identifiant.
     */
    @Override
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     * Recherche un membre par son numéro unique.
     */
    @Override
    public Optional<Member> getMemberByNumber(String memberNumber) {
        return memberRepository.findByMemberNumber(memberNumber);
    }

    /**
     * {@inheritDoc}
     * Met à jour les informations d'un membre existant.
     * @throws RuntimeException si le membre n'existe pas
     */
    @Override
    public Member updateMember(Long id, Member memberDetails) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Membre non trouvé avec l'id: " + id));

        member.setAddress(memberDetails.getAddress());
        member.setBirthdate(memberDetails.getBirthdate());
        member.setPenalties(memberDetails.getPenalties());

        return memberRepository.save(member);
    }

    /**
     * {@inheritDoc}
     * Supprime un membre de la base de données.
     */
    @Override
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     * Vérifie si un numéro de membre est déjà utilisé.
     */
    @Override
    public boolean existsByMemberNumber(String memberNumber) {
        return memberRepository.findByMemberNumber(memberNumber).isPresent();
    }
}