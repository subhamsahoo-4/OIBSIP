package com.library.dto.response;

import java.time.LocalDate;

import com.library.entity.IssueRecord;

public record IssueResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long memberId,
        String memberName,
        LocalDate issueDate,
        LocalDate dueDate,
        LocalDate returnDate,
        boolean returned
) {

    public static IssueResponse from(IssueRecord i) {
        return new IssueResponse(
                i.getId(),
                i.getBook().getId(),
                i.getBook().getTitle(),
                i.getMember().getId(),
                i.getMember().getName(),
                i.getIssueDate(),
                i.getDueDate(),
                i.getReturnDate(),
                i.isReturned()
        );
    }
}