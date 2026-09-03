package com.library.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.library.entity.Member;
import com.library.entity.Notification;
import com.library.exception.ResourceNotFoundException;
import com.library.repository.MemberRepository;
import com.library.repository.NotificationRepository;
import com.library.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notifications;
    private final MemberRepository members;

    public NotificationServiceImpl(
            NotificationRepository notifications,
            MemberRepository members) {
        this.notifications = notifications;
        this.members = members;
    }

    @Override
    public Notification create(Long memberId, String type, String message) {
        Member member = members.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        Notification notification = new Notification();
        notification.setMember(member);
        notification.setType(type == null || type.isBlank() ? "INFO" : type.trim().toUpperCase());
        notification.setMessage(message == null ? "" : message.trim());
        notification.setRead(false);

        return notifications.save(notification);
    }

    @Override
    public void createForAdmins(String type, String message) {
        List<Member> admins = members.findAll().stream()
                .filter(m -> m.getRole() == Member.Role.ADMIN && m.isActive())
                .toList();

        for (Member admin : admins) {
            create(admin.getId(), type, message);
        }
    }

    @Override
    public List<Notification> mine(Long memberId) {
        return notifications.findTop50ByMemberIdOrderByCreatedAtDesc(memberId);
    }

    @Override
    public long unreadCount(Long memberId) {
        return notifications.countByMemberIdAndReadFalse(memberId);
    }

    @Override
    @Transactional
    public void markRead(Long notificationId, Long memberId) {
        Notification notification = notifications.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        if (!notification.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("You cannot update this notification");
        }

        notification.setRead(true);
        notifications.save(notification);
    }

    @Override
    @Transactional
    public void markAllRead(Long memberId) {
        notifications.markAllReadByMemberId(memberId);
    }
}
