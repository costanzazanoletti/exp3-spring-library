package org.learning.springlibrary;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.learning.springlibrary.model.Book;
import org.learning.springlibrary.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Seeder implements CommandLineRunner {

  @Autowired
  BookRepository bookRepository;

  @Override
  public void run(String... args) throws Exception {
    Faker faker = new Faker();
    FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-US"),
        new RandomService());

    List<Book> books = new ArrayList<>(100);
    for (int i = 0; i < 100; i++) {
      // create a fake Book
      com.github.javafaker.Book fakeBook = faker.book();
      Book book = new Book();
      book.setAuthors(fakeBook.author());
      book.setTitle(fakeBook.title());
      book.setPublisher(fakeBook.publisher());
      book.setSynopsis(faker.backToTheFuture().quote());
      book.setIsbn(fakeValuesService.regexify("[0-9]{13}"));
      book.setYear(faker.number().numberBetween(1700, LocalDate.now().getYear()));
      book.setNumberOfCopies(faker.number().numberBetween(1, 20));
      book.setCreatedAt(
          faker.date().past(3650, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault())
              .toLocalDateTime());
      // add book to list
      books.add(book);
    }
    // persist list of books
    bookRepository.saveAll(books);
  }
}
