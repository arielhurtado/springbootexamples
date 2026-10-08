package com.example.bookings.service;

import com.example.bookings.infra.config.RabbitMQConfig;
import com.example.bookings.entity.User;
import com.example.bookings.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receiveMessage(User user) {
        // Guardar el usuario en el repositorio local
        try {
            userRepository.save(user);
            System.out.println("Recibido usuario: " + user.getFirstName() + " " + user.getLastName());
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());

        }

    }
}
