package br.com.unifalmg.application.unit;

import br.com.unifalmg.application.entity.Author;
import br.com.unifalmg.application.entity.Book;
import br.com.unifalmg.application.exception.AuthorNotFoundException;
import br.com.unifalmg.application.exception.InvalidAuthorException;
import br.com.unifalmg.application.repository.AuthorRepository;
import br.com.unifalmg.application.service.AuthorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @InjectMocks
    private AuthorService service;

    @Mock
    private AuthorRepository repository;

    @Test
    @DisplayName("#getAuthor > When the id is null > Throw an exception")
    void getAuthorWhenTheIdIsNullThrowAnException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getAuthor(null));
    }

    @Test
    @DisplayName("#getAuthor > When the id is not null > When a author is found > Return the author")
    void getAuthorWhenTheIdIsNotNullWhenAAuthorIsFoundReturnTheUser() {
        when(repository.findById(1)).thenReturn(Optional.of(Author.builder()
                        .id(1)
                        .first_name("Leonardo")
                        .last_name("Pereira")
                        .nationality("Brazil")
                .build()));
        Author response = service.getAuthor(1);
        assertAll(
                () -> assertEquals(1, response.getId()),
                () -> assertEquals("Leonardo", response.getFirst_name()),
                () -> assertEquals("Pereira", response.getLast_name()),
                () -> assertEquals("Brazil", response.getNationality())
        );
    }

    @Test
    @DisplayName("#getAuthor > When the id is not null > When no author is found > Throw an exception")
    void getAuthorWhenTheIdIsNotNullWhenNoAuthorIsFoundThrowAnException() {
        when(repository.findById(2)).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class, () ->
                service.getAuthor(2));
    }

    @Test
    @DisplayName("#getAuthors > When there are no authors > Return empty list")
    void getAuthorsWhenThereAreNoAuthorsReturnEmptyList(){
        when(repository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), service.getAuthors());
    }

    @Test
    @DisplayName("#deleteAuthor > When confirmationDelete is not True > Throw an exception")
    void deleteAuthorWhenConfirmationDeleteIsFalseThrowAnException(){
        assertAll(
                () -> assertThrows(ResponseStatusException.class,
                        () -> service.deleteAuthor(1, Boolean.FALSE)),
                () -> assertThrows(ResponseStatusException.class,
                        () -> service.deleteAuthor(2, null))
        );
    }

    @Test
    @DisplayName("#deleteAuthor > When id is invalid > Throw an exception")
    void deleteAuthorWhenIsIsInvalidThrowAnException(){
        when(repository.findById(50)).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class,
                () -> service.deleteAuthor(50, Boolean.TRUE));
    }

    @Test
    @DisplayName("#add > When author data is null > Throw an exception")
    void addWhenAuthorDataIsNullThrowAnException(){
        List<Book> books = null;
        Author author = null;
        assertAll(
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.add(author)),
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.add(new Author(1,null, "Silva", "Brazil", books))),
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.add(new Author(2,"Pedro", null, "Brazil", books))),
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.add(new Author(3,"Pedro", "Silva", null, books)))
        );
    }

    @Test
    @DisplayName("#add > When author is add > Return author")
    void addWhenAuthorIsAddReturnAuthor(){
        List<Book> books = null;
        Author author = new Author(1,"Pedro", "Silva", "Brazil", books);
        when(repository.save(author)).thenReturn(author);
        Author authorSaved = service.add(author);
        assertAll(
                () -> assertEquals(authorSaved.getId(), author.getId()),
                () -> assertEquals(authorSaved.getFirst_name(), author.getFirst_name()),
                () -> assertEquals(authorSaved.getLast_name(), author.getLast_name()),
                () -> assertEquals(authorSaved.getNationality(), author.getNationality())
        );
    }

    @Test
    @DisplayName("#editAuthor > When author data is null > Throw an exception")
    void editAuthorWhenAuthorDataIsNullThrowAnException(){
        List<Book> books = null;
        Author author = null;
        assertAll(
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.editAuthor(author)),
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.editAuthor(new Author(1,null, "Silva", "Brazil", books))),
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.editAuthor(new Author(2,"Pedro", null, "Brazil", books))),
                () -> assertThrows(InvalidAuthorException.class,
                        () -> service.editAuthor(new Author(3,"Pedro", "Silva", null, books)))
        );
    }

    @Test
    @DisplayName("#editAuthor > When author is edited > Return author")
    void editAuthorWhenAuthorIsEditedReturnAuthor(){
        List<Book> books = null;
        Author author = new Author(1,"Pedro", "Silva", "Brazil", books);
        when(repository.save(author)).thenReturn(author);
        when(repository.findById(1)).thenReturn(Optional.of(author));
        Author authorSaved = service.editAuthor(author);
        assertAll(
                () -> assertEquals(authorSaved.getId(), author.getId()),
                () -> assertEquals(authorSaved.getFirst_name(), author.getFirst_name()),
                () -> assertEquals(authorSaved.getLast_name(), author.getLast_name()),
                () -> assertEquals(authorSaved.getNationality(), author.getNationality())
        );
    }

    @Test
    @DisplayName("#editAuthor > When author not exist > Is create new author > Return author")
    void editAuthorWhenAuthorNotExistIsCreateNewAuthorReturnAuthor(){
        List<Book> books = null;
        Author author = new Author(1,"Pedro", "Silva", "Brazil", books);
        when(repository.save(author)).thenReturn(author);
        when(repository.findById(1)).thenReturn(Optional.empty());
        Author authorSaved = service.editAuthor(author);
        assertAll(
                () -> assertEquals(authorSaved.getId(), author.getId()),
                () -> assertEquals(authorSaved.getFirst_name(), author.getFirst_name()),
                () -> assertEquals(authorSaved.getLast_name(), author.getLast_name()),
                () -> assertEquals(authorSaved.getNationality(), author.getNationality())
        );
    }
}
