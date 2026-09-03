package com.library.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="contact_queries")
public class ContactQuery {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) private Member member;
    @Column(nullable=false) private String subject;
    @Column(nullable=false, length=4000) private String message;
    @Column(nullable=false) private LocalDateTime createdAt=LocalDateTime.now();
    public ContactQuery() {}

    public Long getId(){
        return id;
    }
     
    public void setId(Long v){
        id=v;
    }
    
    public Member getMember(){
        return member;
    }
     
    public void setMember(Member v){
        member=v;
    }
    
    public String getSubject(){
        return subject;
    }
    
    public void setSubject(String v){
        subject=v;
    }
    
    public String getMessage(){
        return message;
    }
    
     public void setMessage(String v){
        message=v;
    }
    
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
     
    public void setCreatedAt(LocalDateTime v){
        createdAt=v;
    }
    
}
