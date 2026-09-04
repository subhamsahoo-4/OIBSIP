package com.library.service;

import com.library.entity.Member;

public interface AuthService {

    Member register(String name, String email, String password);

    Member register(String name, String email, String password, Member.Role role, String adminRegistrationCode);

    Member authenticate(String email, String password);

    Member getByEmail(String email);

    void sendResetCode(String email);

    void resetPassword(
            String email,
            String code,
            String newPassword
    );
}