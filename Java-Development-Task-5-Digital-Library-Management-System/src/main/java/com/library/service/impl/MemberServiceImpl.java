package com.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entity.Member;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import com.library.service.MemberService;
import com.library.service.NotificationService;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repo;
    private final NotificationService notifications;

    public MemberServiceImpl(MemberRepository repo, NotificationService notifications) {
        this.repo = repo;
        this.notifications = notifications;
    }

    public List<Member> all() {
        return repo.findAll();
    }

    public Member get(Long id) {
        return repo
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Member not found")
                );
    }

    public Member setActive(Long id, boolean active) {
        Member m = get(id);

        if (m.getRole() == Member.Role.ADMIN && !active) {
            throw new IllegalArgumentException(
                    "Admin account cannot be deactivated"
            );
        }

        m.setActive(active);

        Member saved = repo.save(m);
        notifications.create(
                saved.getId(),
                active ? "ACCOUNT" : "WARNING",
                active ? "Your library account has been activated." : "Your library account has been deactivated."
        );
        notifications.createForAdmins(
                "MEMBER",
                "Member " + saved.getName() + " was " + (active ? "activated" : "deactivated") + "."
        );

        return saved;
    }
}