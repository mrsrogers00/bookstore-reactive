package com.iwallet.bookstore.bookTest;

import com.iwallet.bookstore.book.controller.BookRestHandler;
import com.iwallet.bookstore.book.controller.ValidatorHandler;
import com.iwallet.bookstore.book.dto.Book;
import com.iwallet.bookstore.book.service.BookServiceImp;
import com.iwallet.bookstore.user.controller.UserRestHandler;
import com.iwallet.bookstore.user.dto.User;
import com.iwallet.bookstore.user.service.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class BookRestHandlerTest {

    private BookRestHandler bookRestHandler;
    private BookServiceImp bookService;
    private ServerRequest serverRequest;

    @BeforeEach
    public void setUp() {
        bookService = mock(BookServiceImp.class);
        ValidatorHandler validator = mock(ValidatorHandler.class);
        bookRestHandler = new BookRestHandler(validator, bookService);
        serverRequest = mock(ServerRequest.class);
    }

    @Test
    public void testGetAll() {
        List<Book> bookList = new ArrayList<>();
        bookList.add(new Book(1L, "John", "Doe", 50));
        when(bookService.getBookInformations()).thenReturn(Flux.fromIterable(bookList));

        Mono<ServerResponse> responseMono = bookRestHandler.getAll(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(bookService, times(1)).getBookInformations();
    }

    @Test
    public void testGetBookInformationById() {
        Book book = new Book(1L, "John", "Doe", 50);
        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(bookService.getBookInformationById("1")).thenReturn(Mono.just(book));

        Mono<ServerResponse> responseMono = bookRestHandler.getBookInformationById(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(bookService, times(1)).getBookInformationById("1");
    }

    @Test
    public void testCreateBookInformation() {
        Book book = new Book(1L, "John", "Doe", 50);
        when(bookService.saveBookInformation(any(Book.class))).thenReturn(Mono.just(book));
        when(serverRequest.bodyToMono(Book.class)).thenReturn(Mono.just(book));

        Mono<ServerResponse> responseMono = bookRestHandler.createBookInformation(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(bookService, times(1)).saveBookInformation(any(Book.class));
    }
    @Test
    public void testUpdateBookInformation() {
        Book book = new Book(1L, "John", "Doe", 50);
        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(bookService.updateBookInformation(eq("1"), any(Book.class))).thenReturn(Mono.just(book));
        when(serverRequest.bodyToMono(Book.class)).thenReturn(Mono.just(book));

        Mono<ServerResponse> responseMono = bookRestHandler.updateBookInformation(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(bookService, times(1)).updateBookInformation(eq("1"), any(Book.class));
    }

    @Test
    public void testDeleteUserInformation() {
        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(bookService.deleteBookInformation("1")).thenReturn(Mono.empty());

        Mono<Void> responseMono = bookRestHandler.deleteBookInformation(serverRequest).then();
        StepVerifier.create(responseMono)
                .verifyComplete();

        verify(bookService, times(1)).deleteBookInformation("1");
    }

}
