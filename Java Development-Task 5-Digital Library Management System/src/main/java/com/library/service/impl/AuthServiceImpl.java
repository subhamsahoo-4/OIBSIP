package com.library.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.library.entity.Member;
import com.library.entity.PasswordResetCode;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import com.library.repository.PasswordResetCodeRepository;
import com.library.service.AuthService;
import com.library.service.NotificationService;

@Service
public class AuthServiceImpl implements AuthService {

    private final MemberRepository repo;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;
    private final PasswordResetCodeRepository resetCodeRepository;
    private final NotificationService notifications;

    private final SecureRandom secureRandom = new SecureRandom();

    public AuthServiceImpl(
            MemberRepository repo,
            PasswordEncoder encoder,
            JavaMailSender mailSender,
            PasswordResetCodeRepository resetCodeRepository,
            NotificationService notifications) {

        this.repo = repo;
        this.encoder = encoder;
        this.mailSender = mailSender;
        this.resetCodeRepository = resetCodeRepository;
        this.notifications = notifications;
    }


    @Override
    public Member register(
            String name,
            String email,
            String password) {

        if (name == null ||
                name.isBlank() ||
                email == null ||
                email.isBlank() ||
                password == null ||
                password.length() < 6) {

            throw new IllegalArgumentException(
                    "Name, email and a password of at least 6 characters are required"
            );
        }

        if (repo.findByEmailIgnoreCase(email).isPresent()) {

            throw new IllegalArgumentException(
                    "Email is already registered"
            );
        }

        Member m = new Member();

        m.setName(name.trim());
        m.setEmail(email.trim().toLowerCase());
        m.setPassword(encoder.encode(password));
        m.setRole(Member.Role.USER);
        m.setActive(true);

        Member saved = repo.save(m);
        notifications.create(saved.getId(), "ACCOUNT", "Welcome to Digital Library, " + saved.getName() + "!");
        notifications.createForAdmins("MEMBER", "New member registered: " + saved.getName() + ".");
        return saved;
    }


    @Override
    public Member authenticate(
            String email,
            String password) {

       if (email == null || email.isBlank()
                || password == null || password.isBlank()) {

                throw new IllegalArgumentException(
                        "Email and password are required"
                );
        }

        email = email.trim().toLowerCase();

        Member m = repo.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Invalid email or password"
                        )
                );

        
        if (!m.isActive() ||
                !encoder.matches(password, m.getPassword())) {

            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        return m;
    }


    @Override
    public Member getByEmail(String email) {

        Member member = repo.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Member not found"
                        )
                );

        if (!member.isActive()) {
                throw new IllegalArgumentException(
                        "Your account is inactive"
                );
        }

        return member;
    }


 // SEND PASSWORD RESET CODE


    @Override
    public void sendResetCode(String email) {

        if (email == null || email.isBlank()) {

            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        email = email.trim().toLowerCase();

        // Check whether account exists
        repo.findByEmailIgnoreCase(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No account found with this email"
                        )
                );

        // Generate 6-digit verification code
        String code = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000)
        );

        // Invalidate previous unused code
        resetCodeRepository
                .findTopByEmailAndUsedFalseOrderByIdDesc(email)
                .ifPresent(oldCode -> {

                    oldCode.setUsed(true);
                    resetCodeRepository.save(oldCode);
                });


        PasswordResetCode resetCode =
                new PasswordResetCode();

        resetCode.setEmail(email);
        resetCode.setCode(code);

        // Code expires after 10 minutes
        resetCode.setExpiresAt(
                LocalDateTime.now().plusMinutes(10)
        );

        resetCode.setUsed(false);

        resetCodeRepository.save(resetCode);


        // Send email
        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Digital Library - Password Reset Code"
        );

        message.setText(
                "Hello,\n\n"
                + "Your Digital Library password reset code is:\n\n"
                + code
                + "\n\n"
                + "This code will expire in 10 minutes.\n\n"
                + "If you did not request a password reset, "
                + "please ignore this email.\n\n"
                + "Digital Library"
        );

        mailSender.send(message);
    }


// VERIFY CODE AND RESET PASSWORD

    @Override
    public void resetPassword(
            String email,
            String code,
            String newPassword) {

        if (email == null || email.isBlank()) {

            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        if (code == null || code.isBlank()) {

            throw new IllegalArgumentException(
                    "Verification code is required"
            );
        }

        if (newPassword == null ||
                newPassword.length() < 6) {

            throw new IllegalArgumentException(
                    "Password must be at least 6 characters"
            );
        }

        email = email.trim().toLowerCase();
        code = code.trim();


        // Find latest unused verification code
        PasswordResetCode resetCode =
                resetCodeRepository
                        .findTopByEmailAndUsedFalseOrderByIdDesc(email)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No verification code found"
                                )
                        );


        // Check expiration
        if (resetCode.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new IllegalArgumentException(
                    "Verification code has expired"
            );
        }


        // Check code
        if (!resetCode.getCode().equals(code)) {

            throw new IllegalArgumentException(
                    "Invalid verification code"
            );
        }


        // Find member
        Member member =
                repo.findByEmailIgnoreCase(email)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No account found with this email"
                                )
                        );


        // IMPORTANT:
        // Hash the new password before storing it
        member.setPassword(
                encoder.encode(newPassword)
        );

        repo.save(member);


        // Prevent code from being reused
        resetCode.setUsed(true);

        resetCodeRepository.save(resetCode);
    }
}