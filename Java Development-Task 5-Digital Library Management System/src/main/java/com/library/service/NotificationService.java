package com.library.service;

import java.util.List;

import com.library.entity.Notification;

public interface NotificationService {

    Notification create(Long memberId, String type, String message);

    void createForAdmins(String type, String message);

    List<Notification> mine(Long memberId);

    long unreadCount(Long memberId);

    void markRead(Long notificationId, Long memberId);

    void markAllRead(Long memberId);
}
