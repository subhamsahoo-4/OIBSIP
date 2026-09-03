package com.library.dto.response;

import java.time.LocalDateTime;

import com.library.entity.ContactQuery;

public record ContactResponse(
        Long id,
        Long memberId,
        String memberName,
        String email,
        String subject,
        String message,
        LocalDateTime createdAt
) {

    public static ContactResponse from(ContactQuery q) {
        return new ContactResponse(
                q.getId(),
                q.getMember().getId(),
                q.getMember().getName(),
                q.getMember().getEmail(),
                q.getSubject(),
                q.getMessage(),
                q.getCreatedAt()
        );
    }
}