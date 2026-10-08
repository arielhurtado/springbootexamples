package com.example.bookings.repository;


import com.example.bookings.entity.Facility;
import com.example.bookings.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Este componente agrega objetos a los repositorios
 */
@Component
public class DataLoader implements CommandLineRunner {


    @Autowired
    UserRepository userRepository;

    @Autowired
    FacilityRepository facilityRepository;


    @Override
    public void run(String... args) throws Exception {
        Facility facility = new Facility();
        facility.setAddress("Calle 12 No. 12.23 Popayan");
        facility.setCity("Popayan");
        facilityRepository.save(facility);

        User user = new User();
        user.setId(1L);
        user.setFirstName("Carlos Manuel");
        user.setLastName("Arteaga Soto");
        user.setFacility(facility);
        userRepository.save(user);
    }
}
