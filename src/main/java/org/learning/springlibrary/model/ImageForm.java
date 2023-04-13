package org.learning.springlibrary.model;

import org.springframework.web.multipart.MultipartFile;

public class ImageForm {

  private MultipartFile multipartFile;

  private Book book;

  public MultipartFile getMultipartFile() {
    return multipartFile;
  }

  public void setMultipartFile(MultipartFile multipartFile) {
    this.multipartFile = multipartFile;
  }

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
  }
}
