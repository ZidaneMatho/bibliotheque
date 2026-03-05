package com.example.bibliotheque.repository;

import com.example.bibliotheque.models.Borrow;
import com.example.bibliotheque.models.BorrowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Long> {

    List<Borrow> findByMemberId(Long memberId);
    List<Borrow> findByBookId(Long bookId);
    List<Borrow> findByStatus(BorrowStatus status);
    List<Borrow> findByMemberIdAndStatus(Long memberId, BorrowStatus status);
    long countByMemberIdAndStatus(Long memberId, BorrowStatus status);
}