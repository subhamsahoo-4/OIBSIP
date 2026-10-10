package com.library.dto.response;

import com.library.entity.Book;

public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        String category,
        int quantity,
        int availableQuantity
) {

    public static BookResponse from(Book b) {
        return new BookResponse(
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getIsbn(),
                b.getCategory(),
                b.getQuantity(),
                b.getAvailableQuantity()
        );
    }
}