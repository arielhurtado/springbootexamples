package com.example.bookings.service;

import com.example.bookings.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    @Transactional
    Optional<User> findById(Long id) throws Exception;

    @Transactional
    List<User> findAllUsers() throws Exception;
}
