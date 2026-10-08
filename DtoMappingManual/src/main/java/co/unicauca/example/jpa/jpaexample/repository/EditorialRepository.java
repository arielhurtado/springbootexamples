package co.unicauca.example.jpa.jpaexample.repository;

import co.unicauca.example.jpa.jpaexample.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EditorialRepository extends JpaRepository<Editorial, Long> {
    Optional<Editorial> findByName(String editorial);
}
