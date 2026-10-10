package com.library.dto.request;

import com.library.entity.Member;

public record RegisterRequest(
        String name,
        String email,
        String password,
        Member.Role role,
        String adminRegistrationCode
) {
    public RegisterRequest(String name, String email, String password) {
        this(name, email, password, Member.Role.USER, null);
    }
}
