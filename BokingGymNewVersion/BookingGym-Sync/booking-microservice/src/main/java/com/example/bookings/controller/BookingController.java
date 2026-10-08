package com.example.bookings.controller;

import com.example.bookings.infra.dto.BookingRequest;
import com.example.bookings.entity.Booking;
import com.example.bookings.service.BookingService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

/*
  Microservicio A que necesita el servicio de consultar un User por Id del
  microservicio B Information
 */
@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/booking")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {
        Booking booking = new Booking();
        booking.setDateCreated(new Date());
        try {
            Booking savedBooking = bookingService.createBooking(bookingRequest.getUserId(), bookingRequest.getGymClassId(), booking);
            return ResponseEntity.ok(savedBooking);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error guardando reserva, Id del usuario no existe.\"}");
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error invocando microservicio.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"" + e.getMessage() + "\"}");
        }

    }

    @GetMapping("/booking/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Optional<Booking> booking = bookingService.findById(id);
            if (booking.isEmpty()) {
                return ResponseEntity.notFound().build();
            } else {
                //booking.get().setUser();
                return ResponseEntity.ok(booking);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }
    }

    @GetMapping("/booking/")
    public ResponseEntity getAllBooking() {
        try {
            return ResponseEntity.ok(bookingService.getAllBooking());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }
    }
}
