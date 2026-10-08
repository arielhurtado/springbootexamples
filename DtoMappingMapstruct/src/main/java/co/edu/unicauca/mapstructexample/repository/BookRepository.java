package co.edu.unicauca.mapstructexample.repository;


import co.edu.unicauca.mapstructexample.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

}
