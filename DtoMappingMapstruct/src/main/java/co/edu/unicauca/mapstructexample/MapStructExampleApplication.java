package co.edu.unicauca.mapstructexample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Aplicación que muestra como mapear una entidad Libro a LibroDto usando
 * la biblioteca Mapstruct
 */
@SpringBootApplication
@ComponentScan(basePackages = "co.edu.unicauca.mapstructexample")
@EnableJpaRepositories(basePackages = "co.edu.unicauca.mapstructexample.repository")
@EntityScan(basePackages = "co.edu.unicauca.mapstructexample.domain.entity")
public class MapStructExampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapStructExampleApplication.class, args);
    }

}
