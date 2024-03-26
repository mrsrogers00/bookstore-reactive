package com.iwallet.bookstore.book.service;

import com.iwallet.bookstore.book.dto.Book;
import com.iwallet.bookstore.book.repository.BookRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookServiceImp implements BookService {

    private final BookRepository repository;

    public BookServiceImp(BookRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Book> getBookInformationById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Book> getBookInformations() {
        return repository.findAll();
    }

    @Override
    public Mono<Book> saveBookInformation(Book BookInformationDTO) {
        return repository.save(BookInformationDTO);
    }

    @Override
    public Mono<Book> updateBookInformation(String id, Book bookMono) {
        return repository.findById(id).flatMap(book -> {
            bookMono.setId(book.getId()); // if there is something else to update do it here.
            return repository.save(bookMono);
        });
    }

    @Override
    public Mono<Void> deleteBookInformation(String id) {
        return repository.deleteById(id);
    }

}
