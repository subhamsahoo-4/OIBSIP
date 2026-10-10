package com.library.service;

import com.library.entity.Book;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.exception.BookUnavailableException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.FineRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.impl.IssueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock private IssueRecordRepository issues;
    @Mock private BookRepository books;
    @Mock private MemberRepository members;
    @Mock private FineRepository fines;
    @Mock private ReservationService reservations;
    @Mock private NotificationService notifications;

    private IssueServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new IssueServiceImpl(issues, books, members, fines, reservations, notifications);
    }

    @Test
    void issueCreatesIssueAndDecrementsAvailability() {
        Book book = book(1L, 2);
        Member member = member(10L, true);
        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(members.findById(10L)).thenReturn(Optional.of(member));
        when(issues.existsByBookIdAndMemberIdAndReturnedFalse(1L, 10L)).thenReturn(false);
        when(books.save(book)).thenReturn(book);
        when(issues.save(any(IssueRecord.class))).thenAnswer(i -> i.getArgument(0));

        IssueRecord result = service.issue(1L, 10L);

        assertEquals(1, book.getAvailableQuantity());
        assertEquals(member, result.getMember());
        assertEquals(book, result.getBook());
        assertEquals(LocalDate.now(), result.getIssueDate());
        assertEquals(LocalDate.now().plusDays(14), result.getDueDate());
        assertFalse(result.isReturned());
        verify(books).save(book);
        verify(issues).save(any(IssueRecord.class));
    }

    @Test
    void deletedBookCannotBeIssued() {
        Book book = book(1L, 1);
        book.setActive(false);
        when(books.findById(1L)).thenReturn(Optional.of(book));

        assertThrows(ResourceNotFoundException.class, () -> service.issue(1L, 10L));
        verifyNoInteractions(members);
        verify(books, never()).save(any());
    }

    @Test
    void unavailableBookCannotBeIssued() {
        Book book = book(1L, 0);
        Member member = member(10L, true);
        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(members.findById(10L)).thenReturn(Optional.of(member));

        assertThrows(BookUnavailableException.class, () -> service.issue(1L, 10L));
        verify(books, never()).save(any());
    }

    @Test
    void inactiveMemberCannotIssue() {
        Book book = book(1L, 1);
        Member member = member(10L, false);
        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(members.findById(10L)).thenReturn(Optional.of(member));

        assertThrows(IllegalArgumentException.class, () -> service.issue(1L, 10L));
        verify(books, never()).save(any());
    }

    @Test
    void duplicateActiveIssueIsRejected() {
        Book book = book(1L, 1);
        Member member = member(10L, true);
        when(books.findById(1L)).thenReturn(Optional.of(book));
        when(members.findById(10L)).thenReturn(Optional.of(member));
        when(issues.existsByBookIdAndMemberIdAndReturnedFalse(1L, 10L)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> service.issue(1L, 10L));
        verify(books, never()).save(any());
    }

    @Test
    void returnBookIncreasesAvailabilityAndCreatesFineWhenOverdue() {
        Book book = book(1L, 0);
        Member member = member(10L, true);
        IssueRecord issue = new IssueRecord();
        issue.setId(5L);
        issue.setBook(book);
        issue.setMember(member);
        issue.setDueDate(LocalDate.now().minusDays(3));
        issue.setReturned(false);

        when(issues.findByIdAndMemberIdAndReturnedFalse(5L, 10L)).thenReturn(Optional.of(issue));
        when(issues.save(issue)).thenReturn(issue);
        when(books.save(book)).thenReturn(book);
        when(fines.save(any())).thenAnswer(i -> i.getArgument(0));

        IssueRecord result = service.returnBook(5L, 10L);

        assertTrue(result.isReturned());
        assertEquals(LocalDate.now(), result.getReturnDate());
        assertEquals(1, book.getAvailableQuantity());
        verify(fines).save(any());
        verify(reservations).fulfillNext(1L);
    }

    @Test
    void onTimeReturnDoesNotCreateFine() {
        Book book = book(1L, 0);
        Member member = member(10L, true);
        IssueRecord issue = new IssueRecord();
        issue.setId(5L);
        issue.setBook(book);
        issue.setMember(member);
        issue.setDueDate(LocalDate.now());
        issue.setReturned(false);

        when(issues.findByIdAndMemberIdAndReturnedFalse(5L, 10L)).thenReturn(Optional.of(issue));
        when(issues.save(issue)).thenReturn(issue);
        when(books.save(book)).thenReturn(book);

        service.returnBook(5L, 10L);

        verify(fines, never()).save(any());
        assertEquals(1, book.getAvailableQuantity());
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
        m.setName("User");
        m.setActive(active);
        return m;
    }
}
