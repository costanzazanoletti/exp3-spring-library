package org.learning.springlibrary.repository;

import org.learning.springlibrary.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {

}
