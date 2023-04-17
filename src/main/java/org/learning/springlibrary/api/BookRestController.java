package org.learning.springlibrary.api;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.model.Borrowing;
import org.learning.springlibrary.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/books")
public class BookRestController {

  @Autowired
  private BookService bookService;

  // lista di tutti i books
  @GetMapping
  public List<Book> list(@RequestParam(name = "q") Optional<String> search) {
    if (search.isPresent()) {
      return bookService.getFilteredBooks(search.get());
    }
    return bookService.getAllBooks();
  }

  // pagina di books
  @GetMapping("/page")
  public Page<Book> page(/*@RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "2") int size*/
      @PageableDefault(page = 0, size = 2) Pageable pageable) {
    // return bookService.getPage(page, size);
    return bookService.getPage(pageable);
  }

  // singolo book
  @GetMapping("/{id}")
  public Book getById(@PathVariable Integer id) {
    try {
      return bookService.getById(id);
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  // create book
  @PostMapping
  public Book create(@Valid @RequestBody Book book) {
    if (!bookService.isValidIsbn(book)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "{\"errors\": \"the isbn must be unique\"}");
    }
    return bookService.createBook(book);
  }

  // update book
  @PutMapping("/{id}")
  public Book update(@PathVariable Integer id, @Valid @RequestBody Book book) {
    if (!bookService.isValidIsbn(book)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          "{\"errors\": \"the isbn must be unique\"}");
    }
    try {
      return bookService.updateBook(book, id);
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
    }
  }

  // delete book
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Integer id) {
    try {
      boolean success = bookService.deleteById(id);
      if (!success) {
        throw new ResponseStatusException(HttpStatus.CONFLICT,
            "Unable to delete book because it has borrowings");
      }
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }


  // lista di tutti i borrowing
  @GetMapping("/{id}/borrowings")
  public List<Borrowing> getBookBorrowings(@PathVariable("id") Integer bookId) {
    Book book = null;
    try {
      book = bookService.getById(bookId);
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    return book.getBorrowings();
  }
}
