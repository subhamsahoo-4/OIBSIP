package com.library.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entity.ContactQuery;

public interface ContactQueryRepository extends JpaRepository<ContactQuery,Long>{ 
    List<ContactQuery> findAllByOrderByCreatedAtDesc(); 
}