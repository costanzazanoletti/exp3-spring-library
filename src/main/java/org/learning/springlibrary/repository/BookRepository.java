package org.learning.springlibrary.repository;

import java.util.List;
import org.learning.springlibrary.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Integer> {

  // select x from Book x where UPPER(x.title) like UPPER('%?1%')
  public List<Book> findByTitleContainingIgnoreCase(String title);

  // ...where UPPER(x.title) like UPPER('%?1%') order by x.title desc
  public List<Book> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String title);

  public boolean existsByIsbnAndIdNot(String isbn, Integer id);

  public boolean existsByIsbn(String isbn);

  public List<Book> findByTitle(String title); //...where x.title = ?1

  @Query("select b from Book b where b.year = 2000")
  public List<Book> findBooksYear2000();

  @Query(value = "select title from books", nativeQuery = true)
  public List<String> getTitles();

}
