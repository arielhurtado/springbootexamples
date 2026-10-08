package com.unicauca.bookings.domain.services;

import com.unicauca.clientproducthttpclient.domain.entities.Booking;
import com.unicauca.bookings.access.IBookingRepository;

public class BookingService {

    IBookingRepository repo;

    public BookingService(IBookingRepository repo) {
        this.repo = repo;
    }

    public Booking findById(int id) {
        return repo.findById(id);
    }
}
