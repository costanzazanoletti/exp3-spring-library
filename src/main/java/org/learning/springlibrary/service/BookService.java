package org.learning.springlibrary.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.learning.springlibrary.exceptions.BookNotFoundException;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.model.Category;
import org.learning.springlibrary.model.Image;
import org.learning.springlibrary.model.ImageForm;
import org.learning.springlibrary.repository.BookRepository;
import org.learning.springlibrary.repository.CategoryRepository;
import org.learning.springlibrary.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  @Autowired
  BookRepository bookRepository;

  @Autowired
  CategoryRepository categoryRepository;

  @Autowired
  ImageRepository imageRepository;

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

    Set<Category> formCategories = getBookCategories(formBook);
    bookToPersist.setCategories(formCategories);
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
    Set<Category> formCategories = getBookCategories(formBook);
    bookToUpdate.setCategories(formCategories);
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

  public Image updateCover(Integer bookId, ImageForm imageForm)
      throws BookNotFoundException, IOException {
    Book book = getById(bookId);
    Image newImage = new Image();
    newImage.setBook(book);
    newImage.setContent(imageForm.getMultipartFile().getBytes());
    return imageRepository.save(newImage);
  }

  public boolean isValidIsbn(Book bookToValidate) {
    // verifico se su database esiste un book con lo stesso isbn di bookToValidate che non sia bookToValidate
    if (bookToValidate.getId() == null) {
      return !bookRepository.existsByIsbn(bookToValidate.getIsbn());
    }
    return !bookRepository.existsByIsbnAndIdNot(bookToValidate.getIsbn(), bookToValidate.getId());
  }

  private Set<Category> getBookCategories(Book formBook) {
    Set<Category> formCategories = new HashSet<>();
    if (formBook.getCategories() != null) {
      for (Category c : formBook.getCategories()) {
        formCategories.add(categoryRepository.findById(c.getId()).orElseThrow());
      }
    }
    return formCategories;
  }
}
