package com.library.dto.response;

import java.time.LocalDateTime;

import com.library.entity.Member;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String role,
        boolean active,
        LocalDateTime createdAt
) {

    public static MemberResponse from(Member m) {
        return new MemberResponse(
                m.getId(),
                m.getName(),
                m.getEmail(),
                m.getRole().name(),
                m.isActive(),
                m.getCreatedAt()
        );
    }
}