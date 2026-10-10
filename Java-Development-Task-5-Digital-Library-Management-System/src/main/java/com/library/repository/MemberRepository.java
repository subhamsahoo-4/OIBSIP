package com.library.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.Member;

public interface MemberRepository extends JpaRepository<Member,Long>{ 
    Optional<Member> findByEmailIgnoreCase(String email); 
    long countByRole(Member.Role role); 
}
