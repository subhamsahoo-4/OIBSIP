package com.library.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.response.MemberResponse;
import com.library.service.MemberService;

@RestController
@RequestMapping("/api/admin/members")
@PreAuthorize("hasRole('ADMIN')")
public class MemberController {

    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping
    public List<MemberResponse> all() {
        return service
                .all()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    @PatchMapping("/{id}/active")
    public MemberResponse active(
            @PathVariable Long id,
            @RequestParam boolean value
    ) {
        return MemberResponse.from(
                service.setActive(id, value)
        );
    }
}