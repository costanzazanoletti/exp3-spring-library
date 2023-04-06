package org.learning.springlibrary.service;

import org.learning.springlibrary.exceptions.BorrowingNotFoundException;
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

  public Borrowing getById(Integer id) throws BorrowingNotFoundException {
    return borrowingRepository.findById(id)
        .orElseThrow(() -> new BorrowingNotFoundException(Integer.toString(id)));
  }

  public Borrowing update(Integer id, Borrowing formBorrowing) throws BorrowingNotFoundException {
    Borrowing borrowingToUpdate = getById(id);
    borrowingToUpdate.setBorrowingDate(formBorrowing.getBorrowingDate());
    borrowingToUpdate.setExpireDate(formBorrowing.getExpireDate());
    borrowingToUpdate.setReturnDate(formBorrowing.getReturnDate());
    return borrowingRepository.save(borrowingToUpdate);
  }

  public void delete(Integer borrowingId) throws BorrowingNotFoundException {
    Borrowing borrowingToDelete = getById(borrowingId);
    borrowingRepository.delete(borrowingToDelete);
  }
}
