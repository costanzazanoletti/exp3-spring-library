package org.learning.springlibrary.security;

import java.util.Optional;
import org.learning.springlibrary.model.User;
import org.learning.springlibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // recupero lo User da database a partire dall'email
    Optional<User> user = userRepository.findByEmail(username);
    if (user.isPresent()) {
      // creo un oggetto UserDetails a partire da quello User
      return new DatabaseUserDetails(user.get());
    } else {
      throw new UsernameNotFoundException("User with email " + username + " not found");
    }
  }
}
