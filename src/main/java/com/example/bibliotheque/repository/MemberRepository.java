package com.example.bibliotheque.repository;

import com.example.bibliotheque.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // UNIQUEMENT ce qui est utilisé dans MemberService
    Optional<Member> findByMemberNumber(String memberNumber);
}