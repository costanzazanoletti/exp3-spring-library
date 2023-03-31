package org.learning.springlibrary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Size(min = 13, max = 13, message = "The isbn size is exactly 13")
  @Column(nullable = false)
  private String isbn;
  @NotEmpty
  private String title;
  @NotEmpty
  private String authors;
  @NotEmpty
  private String publisher;
  @NotNull
  private Integer year;
  @Lob
  private String synopsis;

  @PositiveOrZero
  @Column(name = "copies")
  private Integer numberOfCopies;
  private LocalDateTime createdAt;

  public Book() {
    super();
  }

  public Book(String isbn, String title, String authors, String publisher, Integer year,
      String synopsis, Integer numberOfCopies, LocalDateTime createdAt) {
    this.isbn = isbn;
    this.title = title;
    this.authors = authors;
    this.publisher = publisher;
    this.year = year;
    this.synopsis = synopsis;
    this.numberOfCopies = numberOfCopies;
    this.createdAt = createdAt;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public String getSynopsis() {
    return synopsis;
  }

  public void setSynopsis(String synopsis) {
    this.synopsis = synopsis;
  }

  public Integer getNumberOfCopies() {
    return numberOfCopies;
  }

  public void setNumberOfCopies(Integer numberOfCopies) {
    this.numberOfCopies = numberOfCopies;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
