package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.model.Borrowing;
import org.learning.springlibrary.service.BookService;
import org.learning.springlibrary.service.BorrowingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/borrowings")
public class BorrowingController {

  @Autowired
  private BorrowingService borrowingService;

  @Autowired
  private BookService bookService;

  @GetMapping("/create")
  public String create(@RequestParam(name = "bookId") Optional<Integer> id, Model model) {
    Borrowing borrowing = new Borrowing();
    borrowing.setBorrowingDate(LocalDate.now());
    borrowing.setExpireDate(LocalDate.now().plusMonths(1));
    if (id.isPresent()) {
      try {
        Book book = bookService.getById(id.get());
        borrowing.setBook(book);
      } catch (BookNotFoundException e) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      }
    }
    model.addAttribute("borrowing", borrowing);
    return "/borrowings/create";
  }

  @PostMapping("/create")
  public String doCreate(@Valid @ModelAttribute Borrowing formBorrowing,
      BindingResult bindingResult) {
    // Validazione
    if (bindingResult.hasErrors()) {
      return "/borrowings/create";
    }
    // persisto il borrowing
    Borrowing createdBorrowing = borrowingService.create(formBorrowing);
    return "redirect:/books/" + Integer.toString(createdBorrowing.getBook().getId());
  }
}
