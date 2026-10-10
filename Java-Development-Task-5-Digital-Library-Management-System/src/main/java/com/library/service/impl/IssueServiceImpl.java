package com.library.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.entity.Book;
import com.library.entity.Fine;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.exception.BookUnavailableException;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.BookRepository;
import com.library.repository.FineRepository;
import com.library.repository.IssueRecordRepository;
import com.library.repository.MemberRepository;
import com.library.service.FineCalculationStrategy;
import com.library.service.IssueService;
import com.library.service.ReservationService;
import com.library.service.NotificationService;

@Service
public class IssueServiceImpl implements IssueService {

    private final IssueRecordRepository issues;
    private final BookRepository books;
    private final MemberRepository members;
    private final FineRepository fines;
    private final ReservationService reservations;
    private final NotificationService notifications;

    private final FineCalculationStrategy fineStrategy =
            new FlatRateFineStrategy(5);

    public IssueServiceImpl(
            IssueRecordRepository issues,
            BookRepository books,
            MemberRepository members,
            FineRepository fines,
            ReservationService reservations,
            NotificationService notifications
    ) {
        this.issues = issues;
        this.books = books;
        this.members = members;
        this.fines = fines;
        this.reservations = reservations;
        this.notifications = notifications;
    }

    @Transactional
    public IssueRecord issue(Long bookId, Long memberId) {

        Book b = books
                .findById(bookId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Book not found")
                );

        if (!b.isActive()) {
            throw new ResourceNotFoundException("Book not found");
        }

        Member m = members
                .findById(memberId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Member not found")
                );

        if (!m.isActive()) {
            throw new IllegalArgumentException("Your account is inactive");
        }

        if (b.getAvailableQuantity() <= 0) {
            throw new BookUnavailableException(
                    "Book is currently unavailable. Reserve it instead."
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

        b.setAvailableQuantity(
                b.getAvailableQuantity() - 1
        );

        books.save(b);

        IssueRecord i = new IssueRecord();

        i.setBook(b);
        i.setMember(m);
        i.setIssueDate(LocalDate.now());
        i.setDueDate(LocalDate.now().plusDays(14));
        i.setReturned(false);

        IssueRecord saved = issues.save(i);
        notifications.create(
                m.getId(),
                "ISSUE",
                "Book issued: " + b.getTitle() + ". Due date: " + saved.getDueDate() + "."
        );
        notifications.createForAdmins(
                "ISSUE",
                b.getTitle() + " was issued to " + m.getName() + "."
        );

        return saved;
    }

    @Transactional
    public IssueRecord returnBook(Long issueId, Long memberId) {

        IssueRecord i = issues
                .findByIdAndMemberIdAndReturnedFalse(issueId, memberId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Active issue not found"
                        )
                );

        LocalDate today = LocalDate.now();

        i.setReturnDate(today);
        i.setReturned(true);

        issues.save(i);

        Book b = i.getBook();

        b.setAvailableQuantity(
                b.getAvailableQuantity() + 1
        );

        books.save(b);

        long amount = fineStrategy.calculate(
                i.getDueDate(),
                today
        );

        if (amount > 0) {
            Fine f = new Fine();

            f.setIssueRecord(i);
            f.setMember(i.getMember());
            f.setAmount(amount);
            f.setPaid(false);

            Fine savedFine = fines.save(f);
            notifications.create(
                    i.getMember().getId(),
                    "FINE",
                    "An overdue fine of ₹" + savedFine.getAmount() + " was created for " + b.getTitle() + "."
            );
            notifications.createForAdmins(
                    "FINE",
                    "Overdue fine of ₹" + savedFine.getAmount() + " created for " + i.getMember().getName() + "."
            );
        }

        notifications.create(
                i.getMember().getId(),
                "RETURN",
                "Book returned: " + b.getTitle() + "."
        );
        notifications.createForAdmins(
                "RETURN",
                b.getTitle() + " was returned by " + i.getMember().getName() + "."
        );

        reservations.fulfillNext(b.getId());

        return i;
    }

    public List<IssueRecord> myIssues(Long memberId) {
        return issues.findByMemberIdAndReturnedFalseOrderByDueDateAsc(
                memberId
        );
    }

    public List<IssueRecord> allActive() {
        return issues.findByReturnedFalseOrderByDueDateAsc();
    }
}