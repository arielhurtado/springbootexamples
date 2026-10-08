package co.edu.unicauca.mapstructexample.dto;

import co.edu.unicauca.mapstructexample.domain.entity.Author;
import co.edu.unicauca.mapstructexample.domain.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookMapper mapper = Mappers.getMapper(BookMapper.class);

    @Mapping(source = "editorial.name", target = "editorial")
    @Mapping(source = "authors", target = "authors", qualifiedByName = "mapAuthorsToNames")
    BookDto toDto(Book book);

    /**
     * El método default actúa como una función auxiliar para hacer esta conversión manualmente.
     *
     * @param authors lista de autores
     * @return Lista de Strings de autores
     */
    @Named("mapAuthorsToNames")
    default List<String> mapAutoresToNombres(List<Author> authors) {
        return authors.stream().map(Author::getName).collect(Collectors.toList());
    }

    /**
     * Mapea un DTO a una Entity
     * <p>
     * MapStruct no puede convertir automáticamente un String a un objeto Editorial,
     * ni una lista de String a una lista de Author, por lo que se ignora el mapeo automático
     * para estos campos.
     *
     * @param bookDto DTO book
     * @return entidad Book
     */
    @Mapping(target = "editorial", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Book toEntity(BookDto bookDto);
}