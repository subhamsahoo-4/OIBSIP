package com.library.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.library.entity.Book;
import com.library.entity.Fine;
import com.library.entity.IssueRecord;
import com.library.entity.Member;
import com.library.repository.FineRepository;
import com.library.service.impl.FineServiceImpl;

class FineServiceTest {

    private FineRepository repo;
    private NotificationService notificationService;
    private FineServiceImpl service;

    @BeforeEach
    void setUp() {

        repo = mock(FineRepository.class);

        notificationService = mock(NotificationService.class);

        service = new FineServiceImpl(
                repo,
                notificationService
        );
    }

    @Test
    void markPaidSuccessfully() {

        Member member = new Member();
        member.setId(10L);

        Book book = new Book();
        book.setId(20L);

        IssueRecord issueRecord = new IssueRecord();
        issueRecord.setId(30L);
        issueRecord.setBook(book);

        Fine fine = new Fine();
        fine.setId(1L);
        fine.setPaid(false);
        fine.setMember(member);
        fine.setIssueRecord(issueRecord);

        when(repo.findById(1L)).thenReturn(Optional.of(fine));
        when(repo.save(fine)).thenReturn(fine);

        service.markPaid(1L);

        assertEquals(true, fine.isPaid());

        verify(repo).save(fine);
    }

    @Test
    void markPaidThrowsWhenFineAlreadyPaid() {

        Fine fine = new Fine();
        fine.setId(1L);
        fine.setPaid(true);

        when(repo.findById(1L))
                .thenReturn(Optional.of(fine));

        assertThrows(
                IllegalArgumentException.class,
                () -> service.markPaid(1L)
        );

        verify(repo, never()).save(any(Fine.class));
    }
}