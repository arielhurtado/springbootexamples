package co.edu.unicauca.mapstructexample.controller;

import co.edu.unicauca.mapstructexample.dto.BookDto;
import co.edu.unicauca.mapstructexample.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private IBookService bookService;

    @GetMapping(value = "/book", produces = "application/json")
    public ResponseEntity getLibros() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }

    }

    @PostMapping("/book")
    public ResponseEntity<?> saveBook(@RequestBody BookDto bookDto) {
        try {
            return new ResponseEntity(bookService.saveBook(bookDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar datos.\"}");
        }
    }

}
