package com.library.service;

import java.util.List;

import com.library.dto.request.BookRequest;
import com.library.entity.Book;

public interface BookService { 
    List<Book> list(String category,String search); 
    Book add(BookRequest r); Book update(Long id,BookRequest r); 
    void delete(Long id); Book get(Long id); 
}
