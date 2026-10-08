package com.example.bookings.service;

import com.example.bookings.entity.Booking;
import com.example.bookings.entity.GymClass;
import com.example.bookings.entity.User;
import com.example.bookings.repository.BookingRepository;
import com.example.bookings.repository.GymClassRepository;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService {

    //@Autowired
    private final BookingRepository bookingRepository;
    private final GymClassRepository gymClassRepository;

    private final ServiceInformationClient serviceInformationClient;


    public BookingService(BookingRepository bookingRepository, GymClassRepository gymClassRepository, ServiceInformationClient serviceInformationClient) {

        this.bookingRepository = bookingRepository;
        this.gymClassRepository = gymClassRepository;
        this.serviceInformationClient = serviceInformationClient;
    }

    @Override
    @Transactional
    public Booking createBooking(Long userId, Long gymClassId, Booking booking)  {
        try {
            //Invoca el servicio de Information-Microservice
            System.out.println("Invocando servicio getuserById desde Booking-Microservice");

            User user = serviceInformationClient.getUserById(userId);
            booking.setUser(user);
            booking.setUserId(userId);

            Optional<GymClass> gymClass = gymClassRepository.findById(gymClassId);
            if (gymClass.isEmpty()) {
                throw new EntityNotFoundException("La clase  con ID " + gymClassRepository + " no existe");
            }
            booking.setGymClass(gymClass.get());

            return bookingRepository.save(booking);

        } catch (FeignException.NotFound e) {
            throw new EntityNotFoundException("El usuario con ID " + userId + " no existe");
        } catch (FeignException e) {
            throw new RuntimeException("Error al invocar el microservicio de información: " + e.getMessage(), e);
        } catch (Exception e) {
            // Puedes lanzar una excepción personalizada o simplemente registrar el error
            throw new RuntimeException("Error al crear la reserva: " + e.getMessage(), e);
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

    @Override
    @Transactional
    public Optional<Booking> findById(Long id) throws Exception {
        try {
            return bookingRepository.findById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
