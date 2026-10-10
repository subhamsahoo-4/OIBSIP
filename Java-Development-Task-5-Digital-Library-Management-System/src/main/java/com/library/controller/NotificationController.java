package com.library.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.response.NotificationResponse;
import com.library.service.AuthService;
import com.library.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationService notifications;
    private final AuthService auth;

    public NotificationController(
            NotificationService notifications,
            AuthService auth) {
        this.notifications = notifications;
        this.auth = auth;
    }

    @GetMapping
    public List<NotificationResponse> mine(Authentication authentication) {
        Long memberId = memberId(authentication);

        return notifications.mine(memberId)
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @GetMapping("/unread-count")
    public long unreadCount(Authentication authentication) {
        return notifications.unreadCount(memberId(authentication));
    }

    @PatchMapping("/{id}/read")
    public void markRead(
            @PathVariable Long id,
            Authentication authentication) {
        notifications.markRead(id, memberId(authentication));
    }

    @PatchMapping("/read-all")
    public void markAllRead(Authentication authentication) {
        notifications.markAllRead(memberId(authentication));
    }

    private Long memberId(Authentication authentication) {
        return auth.getByEmail(authentication.getName()).getId();
    }
}
