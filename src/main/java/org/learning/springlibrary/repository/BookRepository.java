package org.learning.springlibrary.repository;

import java.util.List;
import org.learning.springlibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

  public List<Book> findByTitleContainingIgnoreCase(String title);
}
