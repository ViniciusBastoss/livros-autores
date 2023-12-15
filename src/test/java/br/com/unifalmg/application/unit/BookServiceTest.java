package br.com.unifalmg.application.unit;

import br.com.unifalmg.application.entity.Author;
import br.com.unifalmg.application.entity.Book;
import br.com.unifalmg.application.exception.AuthorNotFoundException;
import br.com.unifalmg.application.exception.BookNotFoundException;
import br.com.unifalmg.application.exception.InvalidBookException;
import br.com.unifalmg.application.repository.AuthorRepository;
import br.com.unifalmg.application.repository.BookRepository;
import br.com.unifalmg.application.service.AuthorService;
import br.com.unifalmg.application.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService service;

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private BookRepository bookRepository;

    @Test
    @DisplayName("#getBook > When the id is null > Throw an exception")
    void getBookWhenTheIdIsNullThrowAnException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.getBook(null));
    }

    @Test
    @DisplayName("#getBook > When the id is not null > When a book is found > Return the book")
    void getBookWhenTheIdIsNotNullWhenABookIsFoundReturnTheUser() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(Book.builder()
                .id(1)
                .title("Harry Potter")
                .pages(269)
                .publication_year(1998)
                .build()));
        Book response = service.getBook(1);
        assertAll(
                () -> assertEquals(1, response.getId()),
                () -> assertEquals("Harry Potter", response.getTitle()),
                () -> assertEquals(269, response.getPages()),
                () -> assertEquals(1998, response.getPublication_year())
        );
    }

    @Test
    @DisplayName("#getAuthor > When the id is not null > When no book is found > Throw an exception")
    void getBookWhenTheIdIsNotNullWhenNoBookIsFoundThrowAnException() {
        when(bookRepository.findById(2)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () ->
                service.getBook(2));
    }

    @Test
    @DisplayName("#getBooks > When there are no books > Return empty list")
    void getBooksWhenThereAreNoBooksReturnEmptyList(){
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        assertEquals(Collections.emptyList(), service.getBooks());
    }

    @Test
    @DisplayName("#deleteBook > When confirmationDelete is not True > Throw an exception")
    void deleteBookWhenConfirmationDeleteIsFalseThrowAnException(){
        assertAll(
                () -> assertThrows(ResponseStatusException.class,
                        () -> service.deleteBook(1, Boolean.FALSE)),
                () -> assertThrows(ResponseStatusException.class,
                        () -> service.deleteBook(2, null))
        );
    }

    @Test
    @DisplayName("#deleteBook > When id is invalid > Throw an exception")
    void deleteBookWhenIsIsInvalidThrowAnException(){
        when(bookRepository.findById(50)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,
                () -> service.deleteBook(50, Boolean.TRUE));
    }

    @Test
    @DisplayName("#add > When book data is null > Throw an exception")
    void addWhenBookDataIsNullThrowAnException(){
      Book book = null;
      Author author = null;
        assertAll(
                () -> assertThrows(InvalidBookException.class,
                        () -> service.add(book, 1)),
                () -> assertThrows(InvalidBookException.class,
                        () -> service.add(new Book(1, null, 1998, 256, author), 1)),
                () -> assertThrows(InvalidBookException.class,
                        () -> service.add(new Book(1, "Harry Potter", null, 256, author), 1)),
                () -> assertThrows(InvalidBookException.class,
                        () -> service.add(new Book(1, "Harry Potter", 1998, null, author), 1)));
    }

    @Test
    @DisplayName("#add > When Book is added and author exist > Return book")
    void addWhenBookIsAddedAndAuthorExistReturnBook(){
        Author author = Author.builder()
                .id(1)
                .first_name("Pedro")
                .last_name("Silva")
                .nationality("Brazil").build();
        Book book = Book.builder()
                .id(1)
                .title("Harry Potter")
                .pages(269)
                .publication_year(1998)
                .build();
        when(bookRepository.save(book)).thenReturn(book);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        Book bookSaved = service.add(book, 1);
        assertAll(
                () -> assertEquals(bookSaved.getId(), book.getId()),
                () -> assertEquals(bookSaved.getTitle(), book.getTitle()),
                () -> assertEquals(bookSaved.getPages(), book.getPages()),
                () -> assertEquals(bookSaved.getPublication_year(), book.getPublication_year())
        );
    }

    @Test
    @DisplayName("#add > When Book is added and author not exist > Throw an exception")
    void whenBookIsAddedAndAuthorNotExistThrowAnException(){
        Book book = Book.builder()
                .id(1)
                .title("Harry Potter")
                .pages(269)
                .publication_year(1998)
                .build();
        when(authorRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class,
                () -> service.add(book,1));
    }

    @Test
    @DisplayName("#editBook > When book data is null > Throw an exception")
    void editBookWhenBookDataIsNullThrowAnException(){
        Book book = null;
        Author author = null;
        assertAll(
                () -> assertThrows(InvalidBookException.class,
                        () -> service.editBook(book, 1)),
                () -> assertThrows(InvalidBookException.class,
                        () -> service.editBook(new Book(1, null, 1998, 256, author), 1)),
                () -> assertThrows(InvalidBookException.class,
                        () -> service.editBook(new Book(1, "Harry Potter", null, 256, author), 1)),
                () -> assertThrows(InvalidBookException.class,
                        () -> service.editBook(new Book(1, "Harry Potter", 1998, null, author), 1)));
    }

    @Test
    @DisplayName("#editBook > When Book is edited and author exist > Return book")
    void editBookWhenBookIsEditedAndAuthorExistReturnBook(){
        Author author = Author.builder()
                .id(1)
                .first_name("Pedro")
                .last_name("Silva")
                .nationality("Brazil").build();
        Book bookAntigo = Book.builder()
                .id(1)
                .title("Harry Potter")
                .pages(269)
                .publication_year(1998)
                .build();
        Book book = Book.builder()
                .id(1)
                .title("Harry")
                .pages(240)
                .publication_year(1980)
                .build();
        when(bookRepository.save(bookAntigo)).thenReturn(bookAntigo);
        when(bookRepository.findById(1)).thenReturn(Optional.of(bookAntigo));
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        Book bookSaved = service.editBook(book, 1);
        assertAll(
                () -> assertEquals(bookSaved.getId(), book.getId()),
                () -> assertEquals(bookSaved.getTitle(), book.getTitle()),
                () -> assertEquals(bookSaved.getPages(), book.getPages()),
                () -> assertEquals(bookSaved.getPublication_year(), book.getPublication_year())
        );
    }



    @Test
    @DisplayName("#editBook > When Book is edited and author not exist > Throw an exception")
    void whenBookIsEditedAndAuthorNotExistThrowAnException(){
        Book book = Book.builder()
                .id(1)
                .title("Harry Potter")
                .pages(269)
                .publication_year(1998)
                .build();
        when(authorRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class,
                () -> service.editBook(book,1));
    }

    @Test
    @DisplayName("#editBook > When Book not exist > Throw an exception")
    void whenBookNotExistThrowAnException(){
        Book book = Book.builder()
                .id(1)
                .title("Harry Potter")
                .pages(269)
                .publication_year(1998)
                .build();
        Author author = Author.builder()
                .id(1)
                .first_name("Pedro")
                .last_name("Silva")
                .nationality("Brazil").build();

        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(bookRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(BookNotFoundException.class,
                () -> service.editBook(book,1));
    }
}
