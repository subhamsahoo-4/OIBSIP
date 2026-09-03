package com.library.service;

import com.library.entity.Book;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.entity.Reservation;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.repository.ReservationRepository;
import com.library.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock private ReservationRepository reservations;
    @Mock private BookRepository books;
    @Mock private IssueRecordRepository issues;
    @Mock private MemberRepository members;
    @Mock private NotificationService notifications;

    private ReservationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ReservationServiceImpl(reservations, books, issues, members, notifications);
    }

    @Test
    void deletedBookCannotBeReserved() {
        Book book = book(1L, 0);
        book.setActive(false);
        when(books.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(ResourceNotFoundException.class, () -> service.reserve(1L, 10L));
        verifyNoInteractions(members);
    }

    @Test
    void inactiveMemberCannotReserve() {
        Book book = book(1L, 0);
        Member member = member(10L, false);
        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(members.findById(10L)).thenReturn(Optional.of(member));

        assertThrows(IllegalArgumentException.class, () -> service.reserve(1L, 10L));
        verify(reservations, never()).save(any());
    }

    @Test
    void reservationCanBePlacedForUnavailableActiveBook() {
        Book book = book(1L, 0);
        Member member = member(10L, true);
        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(members.findById(10L)).thenReturn(Optional.of(member));
        when(issues.existsByBookIdAndMemberIdAndReturnedFalse(1L, 10L)).thenReturn(false);
        when(reservations.existsByBookIdAndMemberIdAndFulfilledFalse(1L, 10L)).thenReturn(false);
        when(reservations.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation result = service.reserve(1L, 10L);

        assertEquals(book, result.getBook());
        assertEquals(member, result.getMember());
        assertFalse(result.isFulfilled());
        verify(reservations).save(any(Reservation.class));
    }

    @Test
    void fulfillNextIssuesOldestPendingReservation() {
        Book book = book(1L, 1);
        Member first = member(10L, true);
        Member second = member(20L, true);
        Reservation r1 = reservation(1L, book, first);
        Reservation r2 = reservation(2L, book, second);
        r1.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(2));
        r2.setCreatedAt(java.time.LocalDateTime.now().minusMinutes(1));

        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(reservations.findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(1L))
                .thenReturn(List.of(r1, r2));
        when(books.save(book)).thenReturn(book);
        when(issues.save(any(IssueRecord.class))).thenAnswer(i -> i.getArgument(0));
        when(reservations.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        service.fulfillNext(1L);

        assertEquals(0, book.getAvailableQuantity());
        assertTrue(r1.isFulfilled());
        assertFalse(r2.isFulfilled());
        verify(issues).save(any(IssueRecord.class));
        verify(reservations).save(r1);
    }

    @Test
    void fulfillNextSkipsInactiveReservationMember() {
        Book book = book(1L, 1);
        Member inactive = member(10L, false);
        Member active = member(20L, true);
        Reservation r1 = reservation(1L, book, inactive);
        Reservation r2 = reservation(2L, book, active);

        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(reservations.findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(1L))
                .thenReturn(List.of(r1, r2));
        when(books.save(book)).thenReturn(book);
        when(issues.save(any(IssueRecord.class))).thenAnswer(i -> i.getArgument(0));
        when(reservations.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        service.fulfillNext(1L);

        assertTrue(r2.isFulfilled());
        assertEquals(0, book.getAvailableQuantity());
        verify(reservations).delete(r1);
        verify(issues).save(any(IssueRecord.class));
    }

    private Book book(Long id, int available) {
        Book b = new Book();
        b.setId(id);
        b.setTitle("Java");
        b.setQuantity(Math.max(available, 1));
        b.setAvailableQuantity(available);
        b.setActive(true);
        return b;
    }

    private Member member(Long id, boolean active) {
        Member m = new Member();
        m.setId(id);
        m.setName("User " + id);
        m.setActive(active);
        return m;
    }

    private Reservation reservation(Long id, Book book, Member member) {
        Reservation r = new Reservation();
        r.setId(id);
        r.setBook(book);
        r.setMember(member);
        r.setFulfilled(false);
        return r;
    }
}
