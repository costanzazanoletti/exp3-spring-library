package org.learning.springlibrary.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  @Autowired
  BookRepository bookRepository;

  public Book createBook(Book formBook) {
    Book bookToPersist = new Book();
    bookToPersist.setAuthors(formBook.getAuthors());
    bookToPersist.setTitle(formBook.getTitle());
    bookToPersist.setPublisher(formBook.getPublisher());
    bookToPersist.setIsbn(formBook.getIsbn());
    bookToPersist.setSynopsis(formBook.getSynopsis());
    bookToPersist.setYear(formBook.getYear());
    bookToPersist.setNumberOfCopies(formBook.getNumberOfCopies());
    bookToPersist.setCreatedAt(LocalDateTime.now());
    return bookRepository.save(bookToPersist);
  }

  public Book updateBook(Book formBook, Integer id) throws BookNotFoundException {
    Book bookToUpdate = getById(id);
    bookToUpdate.setTitle(formBook.getTitle());
    bookToUpdate.setAuthors(formBook.getAuthors());
    bookToUpdate.setPublisher(formBook.getPublisher());
    bookToUpdate.setYear(formBook.getYear());
    bookToUpdate.setIsbn(formBook.getIsbn());
    bookToUpdate.setSynopsis(formBook.getSynopsis());
    bookToUpdate.setNumberOfCopies(formBook.getNumberOfCopies());
    return bookRepository.save(bookToUpdate);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll(Sort.by("title"));
  }

  public List<Book> getFilteredBooks(String keyword) {
    return bookRepository.findByTitleContainingIgnoreCase(keyword);
  }

  public Book getById(Integer id) throws BookNotFoundException {
    Optional<Book> result = bookRepository.findById(id);
    if (result.isPresent()) {
      return result.get();
    } else {
      throw new BookNotFoundException(Integer.toString(id));
    }
  }

  public boolean deleteById(Integer id) throws BookNotFoundException {
    bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(Integer.toString(id)));
    try {
      bookRepository.deleteById(id);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public boolean isValidIsbn(Book bookToValidate) {
    // verifico se su database esiste un book con lo stesso isbn di bookToValidate che non sia bookToValidate
    if (bookToValidate.getId() == null) {
      return !bookRepository.existsByIsbn(bookToValidate.getIsbn());
    }
    return !bookRepository.existsByIsbnAndIdNot(bookToValidate.getIsbn(), bookToValidate.getId());
  }
}
