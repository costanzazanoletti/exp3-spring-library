package org.learning.springlibrary.service;

import org.learning.springlibrary.model.Borrowing;
import org.learning.springlibrary.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowingService {

  @Autowired
  private BorrowingRepository borrowingRepository;

  public Borrowing create(Borrowing formBorrowing) {
    return borrowingRepository.save(formBorrowing);
  }
}
