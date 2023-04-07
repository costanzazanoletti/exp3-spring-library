package org.learning.springlibrary.repository;

import java.util.Optional;
import org.learning.springlibrary.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  public Optional<User> findByEmail(String email);
}
