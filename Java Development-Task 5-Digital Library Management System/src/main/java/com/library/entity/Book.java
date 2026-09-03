package com.library.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="books")
public class Book {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String title;
    @Column(nullable=false) private String author;
    @Column(nullable=false, unique=true) private String isbn;
    @Column(nullable=false) private String category;
    @Column(nullable=false) private int quantity;
    @Column(nullable=false) private int availableQuantity;
    @Column(nullable = false) private boolean active = true;

    public Book() {}

    public Long getId(){
        return id;
    }
    
    public void setId(Long v){
        id=v;
    }

    public String getTitle(){
        return title;
    } 
    
    public void setTitle(String v){
        title=v;
    }

    public String getAuthor(){
        return author;
    } 
    
    public void setAuthor(String v){
        author=v;
    }

    public String getIsbn(){
        return isbn;
    } 
    
    public void setIsbn(String v){
        isbn=v;
    }

    public String getCategory(){
        return category;
    } 
    
    public void setCategory(String v){
        category=v;
    }

    public int getQuantity(){
        return quantity;
    } 
    
    public void setQuantity(int v){
        quantity=v;
    }

    public int getAvailableQuantity(){
        return availableQuantity;
    } 
    
    public void setAvailableQuantity(int v){
        availableQuantity=v;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
