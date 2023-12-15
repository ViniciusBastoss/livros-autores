package br.com.unifalmg.application.service;

import br.com.unifalmg.application.entity.Author;
import br.com.unifalmg.application.entity.Book;
import br.com.unifalmg.application.exception.AuthorNotFoundException;
import br.com.unifalmg.application.exception.BookNotFoundException;
import br.com.unifalmg.application.exception.InvalidBookException;
import br.com.unifalmg.application.repository.AuthorRepository;
import br.com.unifalmg.application.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Transactional
    public List<Book> getBooks() {
        return  bookRepository.findAll();
    }

    public Book getBook(Integer id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id null when fetching for an book.");
        }
        return bookRepository.findById(id).orElseThrow(() ->
                new BookNotFoundException(
                        String.format("No book found for id %d", id))
        );
    }
    public Book add(Book book, Integer idAuthor) {
        if (Objects.isNull(book) || Objects.isNull(book.getTitle())
                || Objects.isNull(book.getPages()) || Objects.isNull(book.getPublication_year())) {
            throw new InvalidBookException();
        }

        Optional<Author> existingAuthorOptional = authorRepository.findById(idAuthor);

        if(existingAuthorOptional.isPresent()){
            book.setAuthor(existingAuthorOptional.get());
            return bookRepository.save(book);
        }
        else{
            throw new AuthorNotFoundException();
        }
    }

    public Book editBook(Book book, Integer idAuthor) {
        if (Objects.isNull(book) || Objects.isNull(book.getTitle())
                || Objects.isNull(book.getPages()) || Objects.isNull(book.getPublication_year())) {
            throw new InvalidBookException();
        }

        Optional<Author> existingAuthorOptional = authorRepository.findById(idAuthor);
        Optional<Book> existingBookOptional = bookRepository.findById(book.getId());

        if(!existingAuthorOptional.isPresent()){
            throw new AuthorNotFoundException();
        }
        else{
            if(!existingBookOptional.isPresent()){
                throw new BookNotFoundException();
            }

            else{
                Book editBook = existingBookOptional.get();
                editBook.setAuthor(existingAuthorOptional.get());
                editBook.setPages(book.getPages());
                editBook.setTitle(book.getTitle());
                editBook.setPublication_year(book.getPublication_year());
                return bookRepository.save(editBook);
            }
        }
    }

    public void deleteBook(Integer id, Boolean confirmDeletion) {
        if (confirmDeletion != null && confirmDeletion) {
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new BookNotFoundException());

            bookRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "To delete the book, confirm-deletion must be true.");
        }
    }
}
