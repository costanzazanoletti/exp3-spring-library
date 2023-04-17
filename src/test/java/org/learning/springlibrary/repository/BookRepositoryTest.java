package org.learning.springlibrary.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.learning.springlibrary.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@DataJpaTest
class BookRepositoryTest {

  @Autowired
  private BookRepository bookRepository;

  @Test
  void findByTitleContainingIgnoreCase() {
    List<Book> books = bookRepository.findByTitleContainingIgnoreCase("harry");
    assertEquals(2, books.size());
  }

  @Test
  void findByTitleContainingIgnoreCaseOrderByCreatedAtDesc() {
  }

  @Test
  void existsByIsbnAndIdNot() {
  }

  @Test
  void existsByIsbn() {
  }

  @Test
  void findByTitle() {
  }

  @Test
  void findBooksYear2000() {
  }

  @Test
  void getTitles() {
  }

  @Test
  void pagination() {
    Page<Book> page = bookRepository.findAll(PageRequest.of(0, 2));
    assertEquals(2, page.getContent().size());
  }
}