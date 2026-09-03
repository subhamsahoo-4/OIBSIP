package com.library.service;

import java.util.List;

import com.library.entity.IssueRecord;

public interface IssueService { IssueRecord issue(Long bookId,Long memberId); 
    IssueRecord returnBook(Long issueId,Long memberId); 
    List<IssueRecord> myIssues(Long memberId); 
    List<IssueRecord> allActive(); 
}