package com.library.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.dto.response.ReservationResponse;
import com.library.service.AuthService;
import com.library.service.ReservationService;

@RestController
@RequestMapping("/api/user/reservations")
@PreAuthorize("hasRole('USER')")
public class ReservationController {

    private final ReservationService reservations;
    private final AuthService auth;

    public ReservationController(
            ReservationService reservations,
            AuthService auth
    ) {
        this.reservations = reservations;
        this.auth = auth;
    }

    @PostMapping("/{bookId}")
    public ReservationResponse reserve(
            @PathVariable Long bookId,
            Authentication a
    ) {
        return ReservationResponse.from(
                reservations.reserve(
                        bookId,
                        auth.getByEmail(a.getName()).getId()
                )
        );
    }

    @GetMapping
    public List<ReservationResponse> mine(Authentication a) {
        return reservations
                .myReservations(
                        auth.getByEmail(a.getName()).getId()
                )
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }
}