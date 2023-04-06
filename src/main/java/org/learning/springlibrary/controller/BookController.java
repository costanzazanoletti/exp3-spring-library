package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.model.AlertMessage;
import org.learning.springlibrary.model.AlertMessage.AlertMessageType;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.service.BookService;
import org.learning.springlibrary.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {


  @Autowired
  private BookService bookService;

  @Autowired
  private CategoryService categoryService;

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
    model.addAttribute("categoryList", categoryService.getAll());
    //return "/books/create";
    return "/books/edit";
  }

  @PostMapping("/create")
  public String doCreate(@Valid @ModelAttribute("book") Book formBook,
      BindingResult bindingResult, Model model) {
    // VALIDATION
    boolean hasErrors = bindingResult.hasErrors();
    // custom isbn unique validation
    if (!bookService.isValidIsbn(formBook)) {
      // aggiungo un errore al bindingResult
      bindingResult.addError(new FieldError("book", "isbn", formBook.getIsbn(), false, null, null,
          "isbn must be unique"));
      hasErrors = true;
    }
    if (hasErrors) {
      // ritorno alla view con il form
      // return "/books/create";
      model.addAttribute("categoryList", categoryService.getAll());
      return "/books/edit";
    }
    // se non ci sono errori procedo con la persistenza
    bookService.createBook(formBook);
    return "redirect:/books";
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Integer id, Model model) {
    try {
      Book book = bookService.getById(id);
      model.addAttribute("book", book);
      model.addAttribute("categoryList", categoryService.getAll());
      return "/books/edit";
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book with id " + id + " not found");
    }
  }

  @PostMapping("/edit/{id}")
  public String doEdit(@PathVariable Integer id, @Valid @ModelAttribute("book") Book formBook,
      BindingResult bindingResult, Model model) {
    // VALIDATION
    if (!bookService.isValidIsbn(formBook)) {
      // aggiungo un errore al bindingResult
      bindingResult.addError(new FieldError("book", "isbn", formBook.getIsbn(), false, null, null,
          "isbn must be unique"));
    }
    if (bindingResult.hasErrors()) {
      // ricreo la view pre-compilata
      model.addAttribute("categoryList", categoryService.getAll());
      return "/books/edit";
    }
    // persisto il book
    try {
      Book updatedBook = bookService.updateBook(formBook, id);
      return "redirect:/books/" + Integer.toString(updatedBook.getId());
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "book with id " + id + " not found");
    }
  }


  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
    try {
      boolean success = bookService.deleteById(id);
      if (success) {
        redirectAttributes.addFlashAttribute("message",
            new AlertMessage(AlertMessageType.SUCCESS, "Book with id " + id + " deleted"));

      } else {
        /*throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
            "Unable to delete book with id " + id);*/
        redirectAttributes.addFlashAttribute("message",
            new AlertMessage(AlertMessageType.ERROR, "Unable to delete book with id " + id));
      }

    } catch (BookNotFoundException e) {
      //throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      redirectAttributes.addFlashAttribute("message",
          new AlertMessage(AlertMessageType.ERROR, "Book with id " + id + " not found"));
    }
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
