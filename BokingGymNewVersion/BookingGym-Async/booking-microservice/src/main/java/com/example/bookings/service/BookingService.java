package com.example.bookings.service;


import com.example.bookings.entity.Booking;
import com.example.bookings.entity.GymClass;
import com.example.bookings.entity.User;
import com.example.bookings.repository.BookingRepository;
import com.example.bookings.repository.GymClassRepository;
import com.example.bookings.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GymClassRepository gymClassRepository;


    @Override
    @Transactional
    public Booking createBooking(Long userId, Long gymClassId, Booking booking) throws IllegalAccessException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalAccessException("El usuario con ID " + userId + " no existe");
        }
        booking.setUser(user.get());

        Optional<GymClass> gymClass = gymClassRepository.findById(gymClassId);
        if (gymClass.isEmpty()) {
            throw new IllegalAccessException("La clase  con ID " + gymClassRepository + " no existe");
        }
        booking.setGymClass(gymClass.get());

        return bookingRepository.save(booking);
    }

    @Override
    @Transactional
    public Optional<Booking> findById(Long id) throws Exception {
        try {
            return bookingRepository.findById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<Booking> getAllBooking() throws Exception {
        try {
            return bookingRepository.findAll();

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
