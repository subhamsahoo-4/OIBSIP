package com.library.service;
import java.util.List;

 import com.library.entity.Member;
public interface MemberService { 
    List<Member> all(); 
    Member get(Long id); 
    Member setActive(Long id,boolean active); 
}