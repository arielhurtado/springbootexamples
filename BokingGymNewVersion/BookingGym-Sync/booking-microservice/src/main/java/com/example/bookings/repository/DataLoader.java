package com.example.bookings.repository;

import com.example.bookings.entity.Booking;
import com.example.bookings.entity.GymClass;
import com.example.bookings.entity.Instructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Este componente agrega objetos a los repositorios
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    GymClassRepository gymclassRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    InstructorRepository instructorRepository;

    @Override
    public void run(String... args) throws Exception {
        Instructor instructor = new Instructor();
        //instructor.setId(1L);
        instructor.setName("Miguel Diaz");
        instructorRepository.save(instructor);

        GymClass gymClass = new GymClass();
        gymClass.setDate(new Date());
        gymClass.setDescription("Yoga para principiantes");
        gymClass.setInstuctor(instructor);
        gymclassRepository.save(gymClass);

        Booking booking = new Booking();
        booking.setUserId(1L);
        booking.setDateCreated(new Date());
        booking.setGymClass(gymClass);
        bookingRepository.save(booking);


    }
}
