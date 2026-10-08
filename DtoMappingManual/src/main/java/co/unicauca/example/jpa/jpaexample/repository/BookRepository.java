package co.unicauca.example.jpa.jpaexample.repository;

import co.unicauca.example.jpa.jpaexample.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
