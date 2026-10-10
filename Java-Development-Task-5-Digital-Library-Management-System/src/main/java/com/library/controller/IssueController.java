package com.library.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.response.IssueResponse;
import com.library.service.AuthService;
import com.library.service.IssueService;

@RestController
@RequestMapping("/api")
public class IssueController {

    private final IssueService issues;
    private final AuthService auth;

    public IssueController(
            IssueService issues,
            AuthService auth
    ) {
        this.issues = issues;
        this.auth = auth;
    }

    @GetMapping("/user/issues")
    @PreAuthorize("hasRole('USER')")
    public List<IssueResponse> mine(Authentication a) {
        return issues
                .myIssues(member(a))
                .stream()
                .map(IssueResponse::from)
                .toList();
    }

    @PostMapping("/user/issues/{bookId}")
    @PreAuthorize("hasRole('USER')")
    public IssueResponse issue(
            @PathVariable Long bookId,
            Authentication a
    ) {
        return IssueResponse.from(
                issues.issue(bookId, member(a))
        );
    }

    @PostMapping("/user/issues/{issueId}/return")
    @PreAuthorize("hasRole('USER')")
    public IssueResponse ret(
            @PathVariable Long issueId,
            Authentication a
    ) {
        return IssueResponse.from(
                issues.returnBook(issueId, member(a))
        );
    }

    @GetMapping("/admin/issues")
    @PreAuthorize("hasRole('ADMIN')")
    public List<IssueResponse> all() {
        return issues
                .allActive()
                .stream()
                .map(IssueResponse::from)
                .toList();
    }

    private Long member(Authentication a) {
        return auth.getByEmail(a.getName()).getId();
    }
}

