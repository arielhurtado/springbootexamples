package co.unicauca.example.jpa.jpaexample.service;

import co.unicauca.example.jpa.jpaexample.dto.BookDto;
import co.unicauca.example.jpa.jpaexample.entity.Author;
import co.unicauca.example.jpa.jpaexample.entity.Book;
import co.unicauca.example.jpa.jpaexample.entity.Editorial;
import co.unicauca.example.jpa.jpaexample.repository.AuthorRepository;
import co.unicauca.example.jpa.jpaexample.repository.BookRepository;
import co.unicauca.example.jpa.jpaexample.repository.EditorialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private EditorialRepository editorialRepository;

    /**
     * Consulta todos los libros
     *
     * @return arreglo de libros
     */
    @Override
    @Transactional
    public List<BookDto> getAllBooks() throws Exception {
        try {
            List<Book> books = bookRepository.findAll();

            List<BookDto> booksDtos = books.stream().map(BookDto::new) // Convertir cada Libro a LibroDto de forma manual
                    .collect(Collectors.toList());
            return booksDtos;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Graba un Book
     *
     * @param bookDto DTO book
     * @return DTO de un Book
     */

    @Override
    @Transactional
    public BookDto saveBook(BookDto bookDto) throws Exception {
        try {
            Book book = new Book();
            book.setTitle(bookDto.getTitle());

            // Buscar o crear la editorial
            Editorial editorial = editorialRepository.findByName(bookDto.getEditorial()).orElseGet(() -> {
                Editorial newEditorial = new Editorial();
                newEditorial.setName(bookDto.getEditorial());
                return editorialRepository.save(newEditorial);
            });
            book.setEditorial(editorial);
            List<Author> authors = new ArrayList<>();
            if (bookDto.getAuthors() != null) {
                // Buscar o crear los autores
                authors = bookDto.getAuthors().stream().map(name -> authorRepository.findByName(name).orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(name);
                    return authorRepository.save(newAuthor);
                })).collect(Collectors.toList());
            }
            book.setAuthors(authors);
            Book bookSaved = bookRepository.save(book);
            return toDto(bookSaved);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Convierte un BookDto a una entidad Book
     *
     * @param book
     * @return
     */
    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setEditorial(Optional.ofNullable(book.getEditorial()).map(Editorial::getName).orElse(null));
        bookDto.setAuthors(book.getAuthors().stream().map(Author::getName).collect(Collectors.toList()));
        return bookDto;
    }
}
