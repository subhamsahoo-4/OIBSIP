package com.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service; 

import com.library.entity.ContactQuery;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.ContactQueryRepository;
import com.library.repository.MemberRepository;
import com.library.service.ContactService;
import com.library.service.NotificationService;

@Service public class ContactServiceImpl implements ContactService{
    private final ContactQueryRepository repo; 
    private final MemberRepository members;
    private final NotificationService notifications;

    public ContactServiceImpl(ContactQueryRepository repo, MemberRepository members, NotificationService notifications){
        this.repo=repo;
        this.members=members;
        this.notifications=notifications;
    }
    public ContactQuery create(Long memberId,String subject,String message){
        if(subject==null||subject.isBlank()||message==null||message.isBlank())
            throw new IllegalArgumentException("Subject and message are required");
        ContactQuery q=new ContactQuery();
        q.setMember(members.findById(memberId).orElseThrow(()->new ResourceNotFoundException("Member not found")));
        q.setSubject(subject.trim());
        q.setMessage(message.trim());
        ContactQuery saved = repo.save(q);
        notifications.createForAdmins(
                "MESSAGE",
                "New query from " + saved.getMember().getName() + ": " + saved.getSubject()
        );
        return saved;
    }
    public List<ContactQuery> all(){
        return repo.findAllByOrderByCreatedAtDesc();
    }
}
