package co.edu.unicauca.mapstructexample.service;

import co.edu.unicauca.mapstructexample.domain.entity.Author;
import co.edu.unicauca.mapstructexample.domain.entity.Book;
import co.edu.unicauca.mapstructexample.domain.entity.Editorial;
import co.edu.unicauca.mapstructexample.dto.BookDto;
import co.edu.unicauca.mapstructexample.dto.BookMapper;
import co.edu.unicauca.mapstructexample.repository.AuthorRepository;
import co.edu.unicauca.mapstructexample.repository.BookRepository;
import co.edu.unicauca.mapstructexample.repository.EditorialRepository;
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
    BookRepository bookRepository;

    @Autowired
    EditorialRepository editorialRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public List<BookDto> getAllBooks() throws Exception {
        try {
            List<Book> books = bookRepository.findAll();
            //Mapeo llamando a mapper
            List<BookDto> booksDtos = books.stream().map(book -> BookMapper.mapper.toDto(book)).collect(Collectors.toList());

            return booksDtos;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


    @Override
    public Optional<BookDto> saveBook(BookDto bookDto) throws Exception {
        try {
            Book book = bookMapper.toEntity(bookDto);

            // Buscar o crear la editorial
            Editorial editorial = editorialRepository.findByName(bookDto.getEditorial()).orElseGet(() -> {
                Editorial newEditorial = new Editorial();
                newEditorial.setName(bookDto.getEditorial());
                return editorialRepository.save(newEditorial);
            });
            book.setEditorial(editorial);

            // Buscar o crear los autores
            List<Author> authors = new ArrayList<>();
            if (bookDto.getAuthors() != null) {
                authors = bookDto.getAuthors().stream().map(name -> authorRepository.findByName(name).orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setName(name);
                    return authorRepository.save(newAuthor);
                })).collect(Collectors.toList());
            }
            book.setAuthors(authors);

            Book bookSaved = bookRepository.save(book);
            return Optional.ofNullable(toDto(bookSaved));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public BookDto toDto(Book book) {
        return bookMapper.toDto(book);
    }
}
