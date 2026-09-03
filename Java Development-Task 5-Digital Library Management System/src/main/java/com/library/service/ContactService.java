package com.library.service;
import java.util.List;

import com.library.entity.ContactQuery;

public interface ContactService { 
    ContactQuery create(Long memberId,String subject,String message); 
    List<ContactQuery> all(); 
}