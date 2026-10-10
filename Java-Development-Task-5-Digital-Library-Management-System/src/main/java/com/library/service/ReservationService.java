package com.library.service;

import java.util.List;

import com.library.entity.Reservation;

public interface ReservationService { 
    Reservation reserve(Long bookId,Long memberId); 
    List<Reservation> myReservations(Long memberId); 
    void fulfillNext(Long bookId); 
}
