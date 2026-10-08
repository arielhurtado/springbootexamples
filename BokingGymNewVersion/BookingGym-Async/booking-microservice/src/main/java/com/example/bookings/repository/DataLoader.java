package com.example.bookings.repository;

import com.example.bookings.entity.*;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    FacilityRepository facilityRepository;


    @Override
    public void run(String... args) throws Exception {
        bookingRepository.deleteAll();
        bookingRepository.deleteAll();
        gymclassRepository.deleteAll();
        userRepository.deleteAll();
        instructorRepository.deleteAll();
        facilityRepository.deleteAll();

        Facility facility = new Facility();
        facility.setAddress("Calle 12 No. 12.23 Popayan");
        facility.setCity("Popayan");
        facilityRepository.save(facility);

        Instructor instructor = new Instructor();
        instructor.setName("Miguel Diaz");
        instructorRepository.save(instructor);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Carlos Manuel");
        user.setLastName("Arteaga Soto");
        user.setFacility(facility);
        userRepository.save(user);

        GymClass gymClass = new GymClass();
        gymClass.setDate(new Date());
        gymClass.setDescription("Yoga para principiantes");
        gymClass.setInstuctor(instructor);
        gymclassRepository.save(gymClass);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setDateCreated(new Date());
        booking.setGymClass(gymClass);
        bookingRepository.save(booking);

    }
}
