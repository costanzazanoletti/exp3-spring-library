package org.learning.springlibrary.controller;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.exceptions.BorrowingNotFoundException;
import org.learning.springlibrary.model.AlertMessage;
import org.learning.springlibrary.model.AlertMessage.AlertMessageType;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    return "/borrowings/form";
  }

  @PostMapping("/create")
  public String doCreate(@Valid @ModelAttribute Borrowing formBorrowing,
      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    // Validazione
    if (bindingResult.hasErrors()) {
      return "/borrowings/form";
    }
    // persisto il borrowing
    Borrowing createdBorrowing = borrowingService.create(formBorrowing);
    redirectAttributes.addFlashAttribute("message",
        new AlertMessage(AlertMessageType.SUCCESS, "Borrowing successfully created"));
    return "redirect:/books/" + Integer.toString(createdBorrowing.getBook().getId());
  }

  @GetMapping("/edit/{id}")
  public String edit(@PathVariable Integer id, Model model) {
    try {
      Borrowing borrowing = borrowingService.getById(id);
      model.addAttribute("borrowing", borrowing);
      return "/borrowings/form";
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

  }


  @PostMapping("/edit/{id}")
  public String doEdit(@PathVariable Integer id, @Valid @ModelAttribute Borrowing formBorrowing,
      BindingResult bindingResult, RedirectAttributes redirectAttributes) {
    // testo la validation
    if (bindingResult.hasErrors()) {
      return "/borrowings/form";
    }
    try {
      // persisto le modifiche
      Borrowing updatedBorrowing = borrowingService.update(id, formBorrowing);
      redirectAttributes.addFlashAttribute("message",
          new AlertMessage(AlertMessageType.SUCCESS, "Borrowing successfully updated"));
      return "redirect:/books/" + updatedBorrowing.getBook().getId();
    } catch (BookNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/delete/{id}")
  public String delete(@PathVariable Integer id,
      @RequestParam("bookId") Optional<Integer> bookIdParam,
      RedirectAttributes redirectAttributes) {
    // faccio la delete
    Integer bookId = bookIdParam.get();
    try {
      borrowingService.delete(id);
      redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS,
          "Borrowing successfully deleted"));
    } catch (BorrowingNotFoundException e) {
      redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.ERROR,
          "Borrowing with id " + e.getMessage() + " not found"));
    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.ERROR,
          "Unable to delete borrowing"));
    }
    if (bookId == null) {
      return "redirect:/books";
    }
    return "redirect:/books/" + Integer.toString(bookId);
  }
}
