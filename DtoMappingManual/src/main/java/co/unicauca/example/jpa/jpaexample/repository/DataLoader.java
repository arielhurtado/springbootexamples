package co.unicauca.example.jpa.jpaexample.repository;

import co.unicauca.example.jpa.jpaexample.entity.Author;
import co.unicauca.example.jpa.jpaexample.entity.Book;
import co.unicauca.example.jpa.jpaexample.entity.Editorial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Este componente agrega objetos a los repositorios
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    EditorialRepository editorialRepository;
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {

        Author author = new Author();

        author.setName("Libardo Pantoja");

        Author author2 = new Author();
        author2.setName("Julio Hurtado");

        authorRepository.save(author);
        authorRepository.save(author);

        Editorial editorial = new Editorial();
        editorial.setName("RaMa");

        Book book = new Book();
        book.setTitle("Introduccion a Java");
        book.setEditorial(editorial);
        book.addAuthor(author);
        book.addAuthor(author2);

        authorRepository.save(author);
        authorRepository.save(author2);

        editorialRepository.save(editorial);

        bookRepository.save(book);

    }
}
