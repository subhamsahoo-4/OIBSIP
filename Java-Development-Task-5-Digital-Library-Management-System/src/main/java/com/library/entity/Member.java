
package com.library.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="members")
public class Member {
    public enum Role { ADMIN, USER }
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String email;
    @Column(nullable=false) private String password;
    @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role = Role.USER;
    @Column(nullable=false) private boolean active = true;
    @Column(nullable=false) private LocalDateTime createdAt = LocalDateTime.now();
    public Member() {}
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String v){
        name=v;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String v){
        email=v;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String v){
        password=v;
    }
    public Role getRole(){
        return role;
    }
    public void setRole(Role v){
        role=v;
    }
    public boolean isActive(){
        return active;
    }
    public void setActive(boolean v){
        active=v;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime v){
        createdAt=v;
    }
}
