package com.iwallet.bookstore.book.controller;

import com.iwallet.bookstore.book.dto.Book;
import com.iwallet.bookstore.book.service.BookServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class BookRestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BookRestHandler.class);

    private final ValidatorHandler validator;
    private final BookServiceImp bookService;

    public BookRestHandler(ValidatorHandler validator, BookServiceImp bookService) {
        this.validator = validator;
        this.bookService = bookService;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return bookService.getBookInformations().collectList().flatMap(BookInformations -> {
            if (BookInformations.isEmpty()) {
                return ServerResponse.noContent().build();
            }
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(BookInformations));
        });
    }

    public Mono<ServerResponse> getBookInformationById(ServerRequest request) {
        String id = request.pathVariable("id");

        return bookService.getBookInformationById(id)
                .flatMap(contact -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(contact)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createBookInformation(ServerRequest request) {
        return request.bodyToMono(Book.class)
           //     .doOnNext(validator::validate)
                .flatMap(bookService::saveBookInformation)
                .doOnSuccess(BookInformationSaved -> LOGGER.info("BookInformation saved with id: {} ", BookInformationSaved.getId()))
                .doOnError(e -> LOGGER.error("Error in saveBookInformation method", e))
                .flatMap(BookInformation -> ServerResponse.created(getToUri(BookInformation)).bodyValue(BookInformation));
    }

    public Mono<ServerResponse> updateBookInformation(ServerRequest request) {
        Mono<Book> BookInformationMono = request.bodyToMono(Book.class);
        String id = request.pathVariable("id");
        return BookInformationMono
                .flatMap(BookInformation -> bookService.updateBookInformation(id, BookInformation))
                .flatMap(BookInformation -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(BookInformation))).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteBookInformation(ServerRequest request) {
        String id = request.pathVariable("id");
        return bookService.deleteBookInformation(id)
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private URI getToUri(Book BookInformationSaved) {
        return UriComponentsBuilder.fromPath(("/{id}"))
                .buildAndExpand(BookInformationSaved.getId()).toUri();
    }

}
