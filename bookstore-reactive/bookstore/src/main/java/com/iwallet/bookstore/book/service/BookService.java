package com.iwallet.bookstore.book.service;

import com.iwallet.bookstore.book.dto.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BookService {
    Mono<Book> getBookInformationById(String id);

    Flux<Book> getBookInformations();

    Mono<Book> saveBookInformation(Book BookInformationDTO);

    Mono<Book> updateBookInformation(String id, Book BookInformationDTO);

    Mono<Void> deleteBookInformation(String id);

}
