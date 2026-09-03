package com.library.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Reservation;
public interface ReservationRepository extends JpaRepository<Reservation,Long>{ 
    Optional<Reservation> findFirstByBookIdAndFulfilledFalseOrderByCreatedAtAsc(Long bookId); 
    List<Reservation> findByMemberIdAndFulfilledFalseOrderByCreatedAtAsc(Long memberId); 
    List<Reservation> findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(Long bookId);
    boolean existsByBookIdAndMemberIdAndFulfilledFalse(Long bookId,Long memberId); 
}