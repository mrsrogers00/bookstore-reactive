package com.iwallet.bookstore.book.repository;

import com.iwallet.bookstore.book.dto.Book;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BookRepository extends ReactiveCrudRepository<Book, String> {}
