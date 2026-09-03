package com.library.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.entity.Book;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.entity.Reservation;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.repository.ReservationRepository;
import com.library.service.NotificationService;
import com.library.service.ReservationService;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservations;
    private final BookRepository books;
    private final IssueRecordRepository issues;
    private final MemberRepository members;
    private final NotificationService notifications;

    public ReservationServiceImpl(
            ReservationRepository reservations,
            BookRepository books,
            IssueRecordRepository issues,
            MemberRepository members,
            NotificationService notifications
    ) {
        this.reservations = reservations;
        this.books = books;
        this.issues = issues;
        this.members = members;
        this.notifications = notifications;
    }

    @Transactional
    public Reservation reserve(Long bookId, Long memberId) {

        Book b = books
                .findById(bookId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Book not found")
                );

        if (!b.isActive()) {
            throw new ResourceNotFoundException("Book not found");
        }

        Member member = booksMember(memberId);

        if (!member.isActive()) {
            throw new IllegalArgumentException("Your account is inactive");
        }

        if (b.getAvailableQuantity() > 0) {
            throw new IllegalArgumentException(
                    "Book is available; issue it directly instead of reserving"
            );
        }

        if (issues.existsByBookIdAndMemberIdAndReturnedFalse(
                bookId,
                memberId
        )) {
            throw new IllegalArgumentException(
                    "You already have this book issued"
            );
        }

        if (reservations.existsByBookIdAndMemberIdAndFulfilledFalse(
                bookId,
                memberId
        )) {
            throw new IllegalArgumentException(
                    "You already reserved this book"
            );
        }

        Reservation r = new Reservation();

        r.setBook(b);
        r.setMember(member);

        Reservation saved = reservations.save(r);
        notifications.create(
                memberId,
                "RESERVATION",
                "Reservation placed for " + b.getTitle() + ". You are in the waiting queue."
        );
        notifications.createForAdmins(
                "RESERVATION",
                "New reservation placed for " + b.getTitle() + " by " + saved.getMember().getName() + "."
        );
        return saved;
    }

    @Transactional
    public void fulfillNext(Long bookId) {

        Book b = books
                .findById(bookId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Book not found"
                        )
                );

        if (b.getAvailableQuantity() <= 0) {
                return;
        }

        List<Reservation> pending =
                reservations
                        .findByBookIdAndFulfilledFalseOrderByCreatedAtAsc(
                                bookId
                        );

        for (Reservation r : pending) {

                Member member = r.getMember();

                // Skip inactive members
                if (!member.isActive()) {
                reservations.delete(r);
                continue;
                }

                // Issue book
                b.setAvailableQuantity(
                        b.getAvailableQuantity() - 1
                );

                books.save(b);

                IssueRecord i = new IssueRecord();

                i.setBook(b);
                i.setMember(member);
                i.setIssueDate(LocalDate.now());
                i.setDueDate(
                        LocalDate.now().plusDays(14)
                );
                i.setReturned(false);

                IssueRecord savedIssue = issues.save(i);

                r.setFulfilled(true);
                reservations.save(r);

                notifications.create(
                        member.getId(),
                        "RESERVATION",
                        "Your reservation for "
                                + b.getTitle()
                                + " has been fulfilled. "
                                + "The book has been issued to you. "
                                + "Due date: "
                                + savedIssue.getDueDate()
                                + "."
                );

                notifications.createForAdmins(
                        "RESERVATION",
                        "Reservation fulfilled for "
                                + member.getName()
                                + ": "
                                + b.getTitle()
                                + "."
                );

                // Only fulfill ONE reservation
                break;
        }
    }
    
    public List<Reservation> myReservations(Long memberId) {
        return reservations.findByMemberIdAndFulfilledFalseOrderByCreatedAtAsc(
                memberId
        );
    }

    private Member booksMember(Long id) {
        return members
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Member not found")
                );
    }
}