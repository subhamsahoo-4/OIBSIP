package com.library.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(
        name = "reservations",
        indexes = @Index(
                name = "idx_res_book_created",
                columnList = "book_id,created_at"
        )
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Book book;

    @ManyToOne(optional = false)
    private Member member;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean fulfilled = false;

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book v) {
        book = v;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member v) {
        member = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean v) {
        fulfilled = v;
    }
}