
package com.library.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.response.FineResponse;
import com.library.service.AuthService;
import com.library.service.FineService;

@RestController
@RequestMapping("/api")
public class FineController {

    private final FineService fines;
    private final AuthService auth;

    public FineController(
            FineService fines,
            AuthService auth
    ) {
        this.fines = fines;
        this.auth = auth;
    }

    @GetMapping("/user/fines")
    @PreAuthorize("hasRole('USER')")
    public List<FineResponse> mine(Authentication a) {
        return fines
                .myFines(auth.getByEmail(a.getName()).getId())
                .stream()
                .map(FineResponse::from)
                .toList();
    }

    @GetMapping("/admin/fines")
    @PreAuthorize("hasRole('ADMIN')")
    public List<FineResponse> all() {
        return fines
                .all()
                .stream()
                .map(FineResponse::from)
                .toList();
    }

    @PatchMapping("/admin/fines/{id}/paid")
    @PreAuthorize("hasRole('ADMIN')")
    public FineResponse paid(@PathVariable Long id) {
        return FineResponse.from(
                fines.markPaid(id)
        );
    }
}


