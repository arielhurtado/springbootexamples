package co.unicauca.example.jpa.jpaexample.controller;

import co.unicauca.example.jpa.jpaexample.dto.BookDto;
import co.unicauca.example.jpa.jpaexample.entity.Editorial;
import co.unicauca.example.jpa.jpaexample.repository.EditorialRepository;
import co.unicauca.example.jpa.jpaexample.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("")
public class BiblioController {
    @Autowired
    private IBookService bookService;

    @Autowired
    private EditorialRepository editorialRepository;

    @GetMapping("/book")
    public ResponseEntity getAllLibros() {
        try {
            return ResponseEntity.ok(bookService.getAllBooks());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error al recuperar datos.\"}");
        }

    }

    @PostMapping("/book")
    public ResponseEntity<?> saveBook(@RequestBody BookDto bookDto) {
        try {
            BookDto bookSavedDto = bookService.saveBook(bookDto);
            return new ResponseEntity(bookSavedDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al guardar datos.\"}");
        }

    }

    /**
     * No es correcto invocar a las editoriales directamente desde el repositorio,
     * es solo un ejemplo simple para ver que la respuesta es ANIDADA y se requiere usar
     * un Dto
     *
     * @return arreglo de editoriales
     */
    @GetMapping("/editorial")
    public List<Editorial> getAllEditoriales() {
        return editorialRepository.findAll();
    }

}
