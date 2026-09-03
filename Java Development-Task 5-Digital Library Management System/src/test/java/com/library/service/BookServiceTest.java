package com.library.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.library.dto.request.BookRequest;
import com.library.entity.Book;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repo;

    @Mock
    private NotificationService notifications;

    @Mock
    private ReservationService reservations;

    private BookServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BookServiceImpl(
                repo,
                notifications,
                reservations
        );
    }

    @Test
    void addBookSetsAvailableQuantity() {

        BookRequest request = new BookRequest(
                "Java",
                "Author",
                "ISBN123",
                "Programming",
                5
        );

        when(repo.findByIsbn("ISBN123"))
                .thenReturn(Optional.empty());

        when(repo.save(any(Book.class)))
                .thenAnswer(i -> i.getArgument(0));

        Book result = service.add(request);

        assertEquals(5, result.getQuantity());
        assertEquals(5, result.getAvailableQuantity());
        assertTrue(result.isActive());

        verify(repo).save(any(Book.class));
    }

    @Test
    void cannotAddDuplicateIsbn() {

        BookRequest request = new BookRequest(
                "Java",
                "Author",
                "ISBN123",
                "Programming",
                5
        );

        when(repo.findByIsbn("ISBN123"))
                .thenReturn(Optional.of(new Book()));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(request)
        );

        verify(repo, never()).save(any());
    }

    @Test
    void cannotAddNegativeQuantity() {

        BookRequest request = new BookRequest(
                "Java",
                "Author",
                "ISBN123",
                "Programming",
                -1
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> service.add(request)
        );

        verify(repo, never()).save(any());
    }

    @Test
    void deletedBookCannotBeReadOrUpdated() {

        Book deleted = book(
                1L,
                "Deleted",
                2,
                2
        );

        deleted.setActive(false);

        when(repo.findById(1L))
                .thenReturn(Optional.of(deleted));

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.get(1L)
        );

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.update(
                        1L,
                        new BookRequest(
                                "New",
                                "Author",
                                "ISBN",
                                "Cat",
                                2
                        )
                )
        );
    }

    @Test
    void deletingBookSoftDeletesIt() {

        Book book = book(
                1L,
                "Java",
                3,
                3
        );

        when(repo.findById(1L))
                .thenReturn(Optional.of(book));

        service.delete(1L);

        assertFalse(book.isActive());

        verify(repo).save(book);
    }

    @Test
    void cannotDeleteBookWithIssuedCopies() {

        Book book = book(
                1L,
                "Java",
                3,
                2
        );

        when(repo.findById(1L))
                .thenReturn(Optional.of(book));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.delete(1L)
        );

        verify(repo, never()).save(any());
    }

    @Test
    void cannotUpdateQuantityBelowIssuedCopies() {

        // 5 total - 2 available = 3 issued
        Book book = book(
                1L,
                "Java",
                5,
                2
        );

        when(repo.findById(1L))
                .thenReturn(Optional.of(book));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.update(
                        1L,
                        new BookRequest(
                                "Java",
                                "Author",
                                "ISBN",
                                "Cat",
                                2
                        )
                )
        );

        verify(repo, never()).save(any());
    }

    @Test
    void increasingAvailabilityFulfillsReservationsForNewCopies() {

        /*
         * BEFORE:
         *
         * Total      = 2
         * Issued     = 0
         * Available  = 2
         *
         * AFTER:
         *
         * Total      = 4
         * Issued     = 0
         * Available  = 4
         *
         * Newly available = 4 - 2 = 2
         *
         * Therefore fulfillNext() must be called twice.
         */

        Book book = book(
                1L,
                "Java",
                2,
                2
        );

        when(repo.findById(1L))
                .thenReturn(Optional.of(book));

        when(repo.findByIsbn("ISBN"))
                .thenReturn(Optional.empty());

        when(repo.save(any(Book.class)))
                .thenAnswer(i -> i.getArgument(0));

        service.update(
                1L,
                new BookRequest(
                        "Java",
                        "Author",
                        "ISBN",
                        "Cat",
                        4
                )
        );

        assertEquals(4, book.getQuantity());
        assertEquals(4, book.getAvailableQuantity());

        verify(reservations, times(2))
                .fulfillNext(1L);
    }

    private Book book(
            Long id,
            String title,
            int quantity,
            int available
    ) {

        Book b = new Book();

        b.setId(id);
        b.setTitle(title);
        b.setAuthor("Author");
        b.setIsbn("ISBN-" + id);
        b.setCategory("Cat");
        b.setQuantity(quantity);
        b.setAvailableQuantity(available);
        b.setActive(true);

        return b;
    }
}