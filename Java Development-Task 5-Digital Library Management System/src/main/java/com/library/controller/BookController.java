package com.library.controller;
import java.util.List;
 
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.request.BookRequest;
import com.library.dto.response.BookResponse;
import com.library.service.BookService;

@RestController @RequestMapping("/api/books") public class BookController{
    private final BookService service; 
    public BookController(BookService service){
        this.service=service;
    }
    @GetMapping public List<BookResponse> list(@RequestParam(required=false)String category,@RequestParam(required=false)String search){
        return service.list(category,search).stream().map(BookResponse::from).toList();
    }
    @GetMapping("/{id}") public BookResponse get(@PathVariable Long id){
        return BookResponse.from(service.get(id));
    }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") public BookResponse add(@RequestBody BookRequest r){
        return BookResponse.from(service.add(r));
    }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public BookResponse update(@PathVariable Long id,@RequestBody BookRequest r){
        return BookResponse.from(service.update(id,r));
    }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public void delete(@PathVariable Long id){
        service.delete(id);
    }
}
