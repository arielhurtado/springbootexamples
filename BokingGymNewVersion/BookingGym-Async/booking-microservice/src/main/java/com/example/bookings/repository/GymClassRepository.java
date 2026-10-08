package com.example.bookings.repository;

import com.example.bookings.entity.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GymClassRepository extends JpaRepository<GymClass, Long> {
}