package br.com.unifalmg.application.controller;

import br.com.unifalmg.application.entity.Author;
import br.com.unifalmg.application.service.AuthorService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@AllArgsConstructor
@RequestMapping("/api/author")
public class AuthorController {

    private final AuthorService authorService;


    @GetMapping("/authors")
    public String author(Model model) {
        List<Author> authors = authorService.getAuthors();
        model.addAttribute("authors", authors);
        return "authors";
    }

    @GetMapping("/authorSearch")
    public String showAuthorForm(Author author) {
        return "author";
    }

    @PostMapping("/authorSearch")
    public String authorSearch(@RequestParam(name = "id") Integer id,
                             Model model) {
        return "redirect:/api/author/" + id;
    }

    @GetMapping("/{id}")
    public String showAuthor(@PathVariable("id") Integer id,
                           Model model) {
        Author author = authorService.getAuthor(id);
        model.addAttribute("author", author);
        return "showauthor";
    }

    @GetMapping("/addauthor")
    public String addauthor(Author author) {
        return "newauthor";
    }

    @PostMapping("/addauthor")
    public String newAuthor(@ModelAttribute("author") Author author) {
        log.info("Entrou no cadastro de Author");
        Author addedAuthor = authorService.add(author);
        return "redirect:/api/author/" + addedAuthor.getId();
    }

    @GetMapping("/editAuthor")
    public String editAuthorr(Author author) {
        return "editAuthor";
    }

    @PostMapping("/editAuthor")
    public String editAuthor(@ModelAttribute("author") Author author) {
        log.info("Entrou na edição de author");
        Author editauthor = authorService.editAuthor(author);
        return "redirect:/api/author/" + editauthor.getId();
    }

    @GetMapping("/deleteAuthor")
    public String deleteAuthorForm(Model model) {
        return "deleteAuthor";
    }

    @PostMapping("/deleteAuthor")
    public String deleteAuthor(@RequestParam(name = "id") Integer id,
                               @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        return "redirect:/api/author/delete/" + id + "?confirm-deletion=" + confirmDeletion.orElse(false);
    }
    @GetMapping("/delete/{id}")
    public String confirmDeleteAuthor(@PathVariable("id") Integer id,
                                      @RequestParam(name = "confirm-deletion", defaultValue = "false") Boolean confirmDeletion,
                                      Model model) {
        authorService.deleteAuthor(id, confirmDeletion);
        return "delecaoRealizada";
    }

/*
    @GetMapping(value = "/teste")
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAuthors() {

        return authorService.getAuthors();
    }

    @GetMapping(value = "/{id}/books")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getStudentsCourse(@PathVariable Integer id) {
        return authorService.getBooksAuthor(id);
    }
    */
}
