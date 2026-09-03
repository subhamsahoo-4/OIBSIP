package com.library.service.impl;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import com.library.service.FineCalculationStrategy;

public class FlatRateFineStrategy implements FineCalculationStrategy {

    private final long perDay;

    public FlatRateFineStrategy(long perDay) {
        this.perDay = perDay;
    }

    public long calculate(
            LocalDate dueDate,
            LocalDate returnDate
    ) {
        long days = Math.max(
                0,
                ChronoUnit.DAYS.between(dueDate, returnDate)
        );

        return days * perDay;
    }
}