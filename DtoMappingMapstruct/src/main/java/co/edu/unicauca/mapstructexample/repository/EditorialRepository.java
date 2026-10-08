package co.edu.unicauca.mapstructexample.repository;


import co.edu.unicauca.mapstructexample.domain.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, Long> {

    Optional<Editorial> findByName(String editorial);
}
