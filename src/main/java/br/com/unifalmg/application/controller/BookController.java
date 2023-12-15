package br.com.unifalmg.application.controller;

import br.com.unifalmg.application.entity.Book;
import br.com.unifalmg.application.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@AllArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    private final BookService bookService;

    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    public String book(Model model) {
        List<Book> books = bookService.getBooks();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/bookSearch")
    public String showBookForm(Book book) {
        return "book";
    }

    @PostMapping("/bookSearch")
    public String bookSearch(@RequestParam(name = "id") Integer id,
                             Model model) {
        return "redirect:/api/book/" + id;
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") Integer id,
                           Model model) {
        Book book = bookService.getBook(id);
        model.addAttribute("book", book);
        return "showbook";
    }

    @GetMapping("/addbook")
    public String addBook(Book book) {
        return "newBook";
    }

    @PostMapping("/addbook")
    public String newBook(@ModelAttribute("book") Book book, Integer idAuthor) {
        Book addedBook = bookService.add(book, idAuthor);
        return "redirect:/api/book/" + addedBook.getId();
    }

    @GetMapping("/editBook")
    public String editBookk(Book book) {
        return "editBook";
    }

    @PostMapping("/editBook")
    public String editBook(@ModelAttribute("book") Book book, Integer idAuthor) {
        log.info("Entrou na edição do book");
        Book editbook = bookService.editBook(book, idAuthor);
        return "redirect:/api/book/" + editbook.getId();
    }

    @GetMapping("/deleteBook")
    public String deleteBookForm(Model model) {
        return "deleteBook";
    }

    @PostMapping("/deleteBook")
    public String deleteBook(@RequestParam(name = "id") Integer id,
                             @RequestParam(name = "confirm-deletion") Optional<Boolean> confirmDeletion) {
        return "redirect:/api/book/delete/" + id + "?confirm-deletion=" + confirmDeletion.orElse(false);
    }

    @GetMapping("/delete/{id}")
    public String confirmDeleteBook(@PathVariable("id") Integer id,
                                    @RequestParam(name = "confirm-deletion", defaultValue = "false") Boolean confirmDeletion,
                                    Model model) {
        bookService.deleteBook(id, confirmDeletion);
        return "delecaoRealizada";
    }


}
