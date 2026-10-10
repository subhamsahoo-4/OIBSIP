package com.library.dto.response;

import java.time.LocalDateTime;

import com.library.entity.Fine;

public record FineResponse(
        Long id,
        Long issueId,
        String bookTitle,
        String memberName,
        long amount,
        boolean paid,
        LocalDateTime createdAt
) {

    public static FineResponse from(Fine f) {
        return new FineResponse(
                f.getId(),
                f.getIssueRecord().getId(),
                f.getIssueRecord().getBook().getTitle(),
                f.getMember().getName(),
                f.getAmount(),
                f.isPaid(),
                f.getCreatedAt()
        );
    }
}