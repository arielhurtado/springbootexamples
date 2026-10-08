package co.edu.unicauca.mapstructexample.service;

import co.edu.unicauca.mapstructexample.dto.BookDto;

import java.util.List;
import java.util.Optional;

public interface IBookService {
    List<BookDto> getAllBooks() throws Exception;

    Optional<BookDto> saveBook(BookDto bookDto) throws Exception;
}
