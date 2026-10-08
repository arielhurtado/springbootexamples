package co.edu.unicauca.mapstructexample.dto;

import java.util.List;

public class BookDto {
    private Long id;
    private String title;

    private String editorial; //Solo el nombre de la editorial

    private List<String> authors; // Solo nombres de autores

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
