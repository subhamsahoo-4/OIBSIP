package com.library.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fines")
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private IssueRecord issueRecord;

    @ManyToOne(optional = false)
    private Member member;

    @Column(nullable = false)
    private long amount;

    @Column(nullable = false)
    private boolean paid = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Fine() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long v) {
        id = v;
    }

    public IssueRecord getIssueRecord() {
        return issueRecord;
    }

    public void setIssueRecord(IssueRecord v) {
        issueRecord = v;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member v) {
        member = v;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long v) {
        amount = v;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean v) {
        paid = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime v) {
        createdAt = v;
    }
}