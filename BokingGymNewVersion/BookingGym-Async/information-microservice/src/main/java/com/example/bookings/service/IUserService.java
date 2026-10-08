package com.example.bookings.service;

import com.example.bookings.infra.dto.UserRequest;
import com.example.bookings.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    @Transactional
    User createUser(UserRequest userRequest) throws Exception;

    @Transactional
    Optional<User> findById(Long id);

    @Transactional
    List<User> findAllUsers() throws Exception;
}
