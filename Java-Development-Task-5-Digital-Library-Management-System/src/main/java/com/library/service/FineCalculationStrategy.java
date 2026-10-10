package com.library.service;
import java.time.LocalDate;

public interface FineCalculationStrategy { 
    long calculate(LocalDate dueDate, LocalDate returnDate); 
}