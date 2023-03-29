package org.learning.springlibrary.controller;

import java.util.List;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public String index(Model model) {
    List<Book> books = bookRepository.findAll();
    model.addAttribute("list", books);
    return "/books/index";
  }

  @GetMapping("/search")
  public String search(Model model, @RequestParam(name = "q") String keyword) {

    List<Book> filteredBooks = bookRepository.findByTitleContainingIgnoreCase(keyword);
    model.addAttribute("list", filteredBooks);
    return "/books/index";
  }
}
