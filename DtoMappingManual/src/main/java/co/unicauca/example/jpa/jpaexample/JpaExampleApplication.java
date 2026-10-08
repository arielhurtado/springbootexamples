package co.unicauca.example.jpa.jpaexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * Aplicación que muestra como mapear una entidad Libro a LibroDto MANUALMENTE,
 * sin usar bibliotecas como Mapstruct o ModelMapper
 */ public class JpaExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaExampleApplication.class, args);
    }

}
