package com.example.bookings.controller;

import com.example.bookings.infra.dto.BookingRequest;
import com.example.bookings.entity.Booking;
import com.example.bookings.entity.User;
import com.example.bookings.service.BookingService;
import com.example.bookings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/*
  Consumidor de mensajes
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setDateCreated(new Date());
        Booking savedBooking = null;
        try {
            savedBooking = bookingService.createBooking(bookingRequest.getUserId(), bookingRequest.getGymClassId(), booking);
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar datos.\"}");
        }
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Optional<Booking> booking = bookingService.findById(id);
            if (booking.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(booking);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }

    }

    // Solicitudes de Usuarios
    @GetMapping("/users")
    public ResponseEntity<?> getAllUser()  {
        try {
            List<User> users = userService.findAllUsers();
            if (users == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(users);
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            Optional<User> user = userService.findById(id);
            if (user.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(user);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }

    }

}
