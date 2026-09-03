package com.library.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Fine;

public interface FineRepository extends JpaRepository<Fine,Long>{ 
    List<Fine> findByMemberIdOrderByCreatedAtDesc(Long memberId); 
    List<Fine> findAllByOrderByPaidAscCreatedAtDesc(); 
    Optional<Fine> findByIssueRecordId(Long issueId); 
}