package com.library.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.entity.Book;
public interface BookRepository extends JpaRepository<Book,Long>{ 
    Optional<Book> findByIsbn(String isbn); 
    @Query("""
        SELECT b FROM Book b
        WHERE b.active = true
        AND (
            LOWER(b.title) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))
        )
        ORDER BY b.title ASC
    """)
    List<Book> searchActiveBooks(@Param("search") String search);

    @Query("""
        SELECT b FROM Book b
        WHERE b.active = true
        AND LOWER(b.category) = LOWER(:category)
        ORDER BY b.title ASC
    """)
    List<Book> findActiveByCategory(@Param("category") String category);

    List<Book> findByActiveTrueOrderByTitleAsc();
    List<Book> findAllByOrderByTitleAsc(); 

}