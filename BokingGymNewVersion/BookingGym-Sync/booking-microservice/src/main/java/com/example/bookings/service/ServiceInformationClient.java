package com.example.bookings.service;

import com.example.bookings.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "information-microservice", url = "http://localhost:8081/api/information")  // La URL del microservicio B
public interface ServiceInformationClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable("id") Long id);
}
