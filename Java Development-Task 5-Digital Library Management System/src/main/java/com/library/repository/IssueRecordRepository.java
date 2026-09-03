package com.library.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.IssueRecord;

public interface IssueRecordRepository extends JpaRepository<IssueRecord,Long>{ 
    List<IssueRecord> findByReturnedFalseOrderByDueDateAsc(); 
    List<IssueRecord> findByMemberIdAndReturnedFalseOrderByDueDateAsc(Long memberId); 
    boolean existsByBookIdAndMemberIdAndReturnedFalse(Long bookId,Long memberId); 
    Optional<IssueRecord> findByIdAndMemberIdAndReturnedFalse(Long id,Long memberId); 
}