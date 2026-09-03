package com.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entity.Fine;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.FineRepository;
import com.library.service.FineService;
import com.library.service.NotificationService;

@Service
public class FineServiceImpl implements FineService {

    private final FineRepository repo;
    private final NotificationService notifications;

    public FineServiceImpl(FineRepository repo, NotificationService notifications) {
        this.repo = repo;
        this.notifications = notifications;
    }

    public List<Fine> myFines(Long memberId) {
        return repo.findByMemberIdOrderByCreatedAtDesc(memberId);
    }

    public List<Fine> all() {
        return repo.findAllByOrderByPaidAscCreatedAtDesc();
    }

    public Fine markPaid(Long id) {
        Fine f = repo
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Fine not found")
                );

        if (f.isPaid()) {
            throw new IllegalArgumentException(
                    "Fine is already paid"
            );
        }

        f.setPaid(true);

        Fine saved = repo.save(f);
        notifications.create(
                saved.getMember().getId(),
                "FINE",
                "Fine paid successfully: ₹" + saved.getAmount() + " for " + saved.getIssueRecord().getBook().getTitle() + "."
        );
        notifications.createForAdmins(
                "FINE",
                "Fine marked paid for " + saved.getMember().getName() + ": ₹" + saved.getAmount() + "."
        );
        return saved;
    }
}