package com.example.bookings.service;

import com.example.bookings.infra.dto.UserRequest;
import com.example.bookings.entity.Facility;
import com.example.bookings.entity.User;
import com.example.bookings.repository.FacilityRepository;
import com.example.bookings.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacilityRepository facilityRepository;

    @Override
    @Transactional
    public User createUser(UserRequest userRequest) throws Exception {
        try {
            // 1. Validar existencia de la instalación
            Optional<Facility> facilityOpt = facilityRepository.findById(userRequest.getFacilityId());
            if (facilityOpt.isEmpty()) {
                throw new IllegalAccessException("La instalación con ID " + userRequest.getFacilityId() + " no existe");
            }

            // 2. Verificar si el ID fue pasado y si ya existe
            if (userRequest.getId() != null) {
                if (userRepository.findById(userRequest.getId()).isPresent()) {
                    throw new IllegalAccessException("El usuario con ID " + userRequest.getId() + " ya existe");
                }
            }

            // 3. Crear y mapear el usuario desde el DTO
            User user = new User();
            user.setId(userRequest.getId());
            user.setFirstName(userRequest.getFirstName());
            user.setLastName(userRequest.getLastName());
            user.setFacility(facilityOpt.get());

            // 4. Guardar el usuario
            User userSaved = userRepository.save(user);

            return userSaved;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    @Override
    @Transactional
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public List<User> findAllUsers() throws Exception {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}