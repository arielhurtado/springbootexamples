package co.unicauca.example.jpa.jpaexample.service;

import co.unicauca.example.jpa.jpaexample.dto.BookDto;

import java.util.List;

public interface IBookService {
    List<BookDto> getAllBooks() throws Exception;

    BookDto saveBook(BookDto bookDto) throws Exception;
}
