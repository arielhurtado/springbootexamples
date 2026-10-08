package com.example.bookings.service;

import com.example.bookings.entity.Booking;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IBookingService {
    @Transactional
    Booking createBooking(Long userId, Long gymClassId, Booking booking) throws Exception;

    @Transactional
    List<Booking> getAllBooking() throws Exception;

    @Transactional
    Optional<Booking> findById(Long id) throws Exception;
}
