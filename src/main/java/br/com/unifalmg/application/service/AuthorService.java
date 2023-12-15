package br.com.unifalmg.application.service;

import br.com.unifalmg.application.entity.Author;
import br.com.unifalmg.application.exception.AuthorNotFoundException;
import br.com.unifalmg.application.exception.InvalidAuthorException;
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
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Transactional
    public List<Author> getAuthors() {
        return  authorRepository.findAll();
    }

    public Author getAuthor(Integer id) {
        if (Objects.isNull(id)) {
            throw new IllegalArgumentException("Id null when fetching for an author.");
        }
        return authorRepository.findById(id).orElseThrow(() ->
                new AuthorNotFoundException(
                        String.format("No author found for id %d", id))
        );
    }

    public Author add(Author author) {
        if (Objects.isNull(author) || Objects.isNull(author.getFirst_name())
                || Objects.isNull(author.getLast_name()) || Objects.isNull(author.getNationality())) {
            throw new InvalidAuthorException();
        }
        return authorRepository.save(author);
    }

    public Author editAuthor(Author author){
        if (Objects.isNull(author) || Objects.isNull(author.getFirst_name())
                || Objects.isNull(author.getLast_name()) || Objects.isNull(author.getNationality())) {
            throw new InvalidAuthorException();
        }
        Optional<Author> existingAuthorOptional = authorRepository.findById(author.getId());

        if (existingAuthorOptional.isPresent()) {
            Author existingAuthor = existingAuthorOptional.get();
            existingAuthor.setNationality(author.getNationality());
            existingAuthor.setFirst_name(author.getFirst_name());
            existingAuthor.setLast_name(author.getLast_name());
            return authorRepository.save(existingAuthor);
        }
        else {
            return authorRepository.save(author);
        }
    }

    public void deleteAuthor(Integer id, Boolean confirmDeletion) {
        if (confirmDeletion != null && confirmDeletion) {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new AuthorNotFoundException());

            authorRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "To delete the author, confirm-deletion must be true.");
        }
    }

}
