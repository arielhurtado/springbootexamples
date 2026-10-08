package com.example.bookings.controller;

import com.example.bookings.infra.dto.UserRequest;
import com.example.bookings.entity.User;
import com.example.bookings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Productor de mensajes
 */
@RestController
@RequestMapping("/api/information")
public class InformationController {
    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody UserRequest userRequest) {
        try {
            try {
                User savedUser = userService.createUser(userRequest);
                return ResponseEntity.ok(savedUser);
            } catch (IllegalAccessException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar datos del usuario.\"}");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUser() {
        try {
            List<User> users = userService.findAllUsers();
            if (users == null) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos del usuario.\"}");
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
