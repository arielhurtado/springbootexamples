package co.unicauca.example.jpa.jpaexample.dto;

import co.unicauca.example.jpa.jpaexample.entity.Author;
import co.unicauca.example.jpa.jpaexample.entity.Book;

import java.util.List;
import java.util.stream.Collectors;


public class BookDto {
    private Long id;
    private String title;

    private String editorial; //Solo el nombre de la editorial

    private List<String> authors; // Solo nombres de autores

    /**
     * Constructor que hace la conversión manual de Boot a BookDto
     * @param book libro
     */
    public BookDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.editorial = book.getEditorial().getName();
        this.authors = book.getAuthors().stream().map(Author::getName).collect(Collectors.toList());

    }

    public BookDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
}
