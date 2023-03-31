package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.learning.springlibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/books")
public class BookController {


  @Autowired
  private BookService bookService;

  @GetMapping
  public String index(Model model, @RequestParam(name = "q") Optional<String> keyword) {
    List<Book> books;
    if (keyword.isEmpty()) {
      books = bookService.getAllBooks();
    } else {
      books = bookService.getFilteredBooks(keyword.get());
      model.addAttribute("keyword", keyword.get());
    }
    model.addAttribute("list", books);

    return "/books/index";
  }


  @GetMapping("/{bookId}")
  public String show(@PathVariable("bookId") Integer id, Model model) {
    try {
      Book book = bookService.getById(id);
      model.addAttribute("book", book);
      return "/books/show";
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found");
    }
    // Book b = bookRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    /*Optional<Book> result = bookRepository.findById(id);
    if (result.isPresent()) {
      model.addAttribute("book", result.get());
      return "/books/show";
    } else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " not found");
    }*/
  }

  @GetMapping("/create")
  public String create(Model model) {
    model.addAttribute("book", new Book());
    return "/books/create";
  }

  @PostMapping("/create")
  public String doCreate(@Valid @ModelAttribute("book") Book formBook,
      BindingResult bindingResult, Model model) {
    // VALIDATION
    if (bindingResult.hasErrors()) {
      // ritorno alla view con il form
      return "/books/create";
    }
    // se non ci sono errori procedo con la persistenza
    bookService.createBook(formBook);
    return "redirect:/books";
  }


  /*@GetMapping("/search")
  public String search(Model model, @RequestParam(name = "q") String keyword) {
    // test
    System.out.println(bookRepository.findByTitle(keyword));
    System.out.println(bookRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword));
    System.out.println(bookRepository.findBooksYear2000());
    System.out.println(bookRepository.getTitles());
    List<Book> filteredBooks = bookRepository.findByTitleContainingIgnoreCase(keyword);

    model.addAttribute("list", filteredBooks);
    return "/books/index";
  }*/
}
