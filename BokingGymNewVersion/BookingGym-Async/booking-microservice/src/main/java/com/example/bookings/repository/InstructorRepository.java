package com.example.bookings.repository;

import com.example.bookings.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}