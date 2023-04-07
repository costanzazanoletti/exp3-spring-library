package org.learning.springlibrary.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

  @Bean
  UserDetailsService userDetailsService() {
    return new DatabaseUserDetailsService();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  DaoAuthenticationProvider authenticationProvider() {
    // creo un authentication provider basato su database (DAO)
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    // associo il mio DatabaseUserDetailsService
    provider.setUserDetailsService(userDetailsService());
    // associo il mio PasswordEncoder
    provider.setPasswordEncoder(passwordEncoder());
    // lo ritorno
    return provider;
  }

  /*
   * home "/" USER, ADMIN
   * books "/books" USER, ADMIN
   * create/edit "/books/create", "/books/edit/{id}" ADMIN
   * delete "/books/delete/{id} ADMIN
   * dettaglio "books/{id} USER, ADMIN
   * borrowings "/borrowings/...." ADMIN
   * categories "/categories/..." ADMIN
   * */
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests()
        .requestMatchers("/categories", "/categories/**").hasAuthority("ADMIN")
        .requestMatchers("/borrowings/**").hasAuthority("ADMIN")
        .requestMatchers("/books/create", "/books/edit/**", "/books/delete/**")
        .hasAuthority("ADMIN")
        .requestMatchers("/", "/books", "/books/**").hasAnyAuthority("USER", "ADMIN")
        .requestMatchers(HttpMethod.POST, "/books/**").hasAuthority("ADMIN")
        .requestMatchers("/**").permitAll()
        .and().formLogin()
        .and().logout()
        .and().exceptionHandling();
    return http.build();
  }
}
