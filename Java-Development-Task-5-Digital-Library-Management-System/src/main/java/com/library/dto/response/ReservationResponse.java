package com.library.dto.response;

import java.time.LocalDateTime;

import com.library.entity.Reservation;

public record ReservationResponse(
        Long id,
        Long bookId,
        String bookTitle,
        LocalDateTime createdAt,
        boolean fulfilled
) {

    public static ReservationResponse from(Reservation r) {
        return new ReservationResponse(
                r.getId(),
                r.getBook().getId(),
                r.getBook().getTitle(),
                r.getCreatedAt(),
                r.isFulfilled()
        );
    }
}