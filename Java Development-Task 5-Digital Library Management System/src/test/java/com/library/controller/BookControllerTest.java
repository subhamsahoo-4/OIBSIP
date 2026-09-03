package com.library.controller;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.entity.Book;
import com.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    private BookService service;
    private BookController controller;

    @BeforeEach
    void setUp() {
        service = mock(BookService.class);
        controller = new BookController(service);
    }

    @Test
    void listReturnsBookResponsesFromService() {
        Book book = book(1L, "Java", 5, 3);
        when(service.list("Programming", "java")).thenReturn(List.of(book));

        List<BookResponse> result = controller.list("Programming", "java");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("Java", result.get(0).title());
        assertEquals(3, result.get(0).availableQuantity());
        verify(service).list("Programming", "java");
    }

    @Test
    void getReturnsBookResponseFromService() {
        Book book = book(1L, "Java", 5, 5);
        when(service.get(1L)).thenReturn(book);

        BookResponse result = controller.get(1L);

        assertEquals(1L, result.id());
        assertEquals("Java", result.title());
        verify(service).get(1L);
    }

    @Test
    void addDelegatesToService() {
        BookRequest request = new BookRequest("Java", "Author", "ISBN", "Programming", 5);
        Book book = book(1L, "Java", 5, 5);
        when(service.add(request)).thenReturn(book);

        BookResponse result = controller.add(request);

        assertEquals(1L, result.id());
        verify(service).add(request);
    }

    @Test
    void updateDelegatesToService() {
        BookRequest request = new BookRequest("Java 2", "Author", "ISBN2", "Programming", 4);
        Book book = book(1L, "Java 2", 4, 4);
        when(service.update(1L, request)).thenReturn(book);

        BookResponse result = controller.update(1L, request);

        assertEquals("Java 2", result.title());
        verify(service).update(1L, request);
    }

    @Test
    void deleteDelegatesToService() {
        controller.delete(1L);
        verify(service).delete(1L);
    }

    private Book book(Long id, String title, int quantity, int available) {
        Book b = new Book();
        b.setId(id);
        b.setTitle(title);
        b.setAuthor("Author");
        b.setIsbn("ISBN-" + id);
        b.setCategory("Programming");
        b.setQuantity(quantity);
        b.setAvailableQuantity(available);
        b.setActive(true);
        return b;
    }
}
