package com.iwallet.bookstore.user.repository;

import com.iwallet.bookstore.book.dto.Book;
import com.iwallet.bookstore.user.dto.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.Optional;

public interface IUserRepository extends ReactiveCrudRepository<User, String> {

    Optional<User> findByUsername(String username);
}

