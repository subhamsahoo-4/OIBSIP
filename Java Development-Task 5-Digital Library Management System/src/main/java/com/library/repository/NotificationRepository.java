package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.library.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop50ByMemberIdOrderByCreatedAtDesc(Long memberId);

    long countByMemberIdAndReadFalse(Long memberId);

    @Modifying
    @Query("update Notification n set n.read = true where n.member.id = :memberId and n.read = false")
    int markAllReadByMemberId(@Param("memberId") Long memberId);
}
