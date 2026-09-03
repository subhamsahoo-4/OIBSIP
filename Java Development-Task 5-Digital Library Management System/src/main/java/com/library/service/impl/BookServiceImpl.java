package com.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import com.library.dto.request.BookRequest;
import com.library.entity.Book;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.service.BookService;
import com.library.service.NotificationService;
import com.library.service.ReservationService;


@Service public class BookServiceImpl implements BookService{
    private final BookRepository repo;
    private final NotificationService notifications;
    private final ReservationService reservations;

    public BookServiceImpl(
            BookRepository repo,
            NotificationService notifications,
            ReservationService reservations
    ){
        this.repo=repo;
        this.notifications=notifications;
        this.reservations=reservations;
    }
    public List<Book> list(String category,String search){
        if(search!=null&&!search.isBlank()){
            return repo.searchActiveBooks(search.trim());
        }
            
        if(category!=null&&!category.isBlank()){
            return repo.findActiveByCategory(category.trim());
        }
            
        return repo.findByActiveTrueOrderByTitleAsc();
    }
    public Book add(BookRequest r){
        validate(r);
        if(r.quantity()<0){
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if(repo.findByIsbn(r.isbn()).isPresent())throw new IllegalArgumentException("ISBN already exists");
        Book b=new Book();apply(b,r);
        b.setAvailableQuantity(r.quantity());
        Book saved = repo.save(b);
        notifications.createForAdmins("BOOK", "New book added: " + saved.getTitle());
        return saved;
    }

    @Transactional
    public Book update(Long id, BookRequest r) {

        validate(r);

        Book b = get(id);

        int issued = b.getQuantity() - b.getAvailableQuantity();

        if (r.quantity() < issued) {
            throw new IllegalArgumentException(
                    "Quantity cannot be less than currently issued copies"
            );
        }

        if (repo.findByIsbn(r.isbn())
                .filter(x -> !x.getId().equals(id))
                .isPresent()) {

            throw new IllegalArgumentException(
                    "ISBN already exists"
            );
        }

        int oldAvailable = b.getAvailableQuantity();

        apply(b, r);

        int newAvailable = r.quantity() - issued;

        b.setAvailableQuantity(newAvailable);

        Book saved = repo.save(b);

        notifications.createForAdmins(
                "BOOK",
                "Book updated: " + saved.getTitle()
        );

        /*
        * If the update creates new available copies,
        * fulfill pending reservations in FIFO order.
        */
        int newlyAvailable = newAvailable - oldAvailable;

        for (int i = 0; i < newlyAvailable; i++) {
            reservations.fulfillNext(saved.getId());
        }

        return saved;
    }

    public void delete(Long id) {

        Book b = get(id);

        if (!b.isActive()) {
            throw new IllegalArgumentException(
                    "Book is already deleted"
            );
        }

        if (b.getAvailableQuantity() != b.getQuantity()) {
            throw new IllegalArgumentException(
                    "Cannot delete a book with issued copies"
            );
        }

        b.setActive(false);

        repo.save(b);

        notifications.createForAdmins(
                "BOOK",
                "Book deleted: " + b.getTitle()
        );
    }
    public Book get(Long id){
        return repo.findById(id)
                .filter(Book::isActive)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }
    private void validate(BookRequest r){
        if(r==null||r.title()==null||r.title().isBlank()||r.author()==null
        ||r.author().isBlank()||r.isbn()==null||r.isbn().isBlank()||r.category()==null||
        r.category().isBlank())throw new IllegalArgumentException("All book fields are required");
        if(r.quantity()<0)throw new IllegalArgumentException("Quantity cannot be negative");
    }
    private void apply(Book b,BookRequest r){
        b.setTitle(r.title().trim());
        b.setAuthor(r.author().trim());
        b.setIsbn(r.isbn().trim());
        b.setCategory(r.category().trim());
        b.setQuantity(r.quantity());
    }
}
