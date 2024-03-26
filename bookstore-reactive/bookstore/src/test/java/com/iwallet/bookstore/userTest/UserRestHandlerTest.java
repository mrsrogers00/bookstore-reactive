package com.iwallet.bookstore.userTest;

import com.iwallet.bookstore.book.controller.ValidatorHandler;
import com.iwallet.bookstore.user.controller.UserRestHandler;
import com.iwallet.bookstore.user.dto.User;
import com.iwallet.bookstore.user.service.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.server.EntityResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class UserRestHandlerTest {

    private UserRestHandler userRestHandler;
    private UserServiceImp userService;
    private ServerRequest serverRequest;

    @BeforeEach
    public void setUp() {
        userService = mock(UserServiceImp.class);
        ValidatorHandler validator = mock(ValidatorHandler.class);
        userRestHandler = new UserRestHandler(validator, userService);
        serverRequest = mock(ServerRequest.class);
    }

    @Test
    public void testGetAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1L, "John", "Doe", "johndoe", "password"));
        when(userService.getUserInformations()).thenReturn(Flux.fromIterable(userList));

        Mono<ServerResponse> responseMono = userRestHandler.getAll(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(userService, times(1)).getUserInformations();
    }


    @Test
    public void testGetUserInformationById() {
        User user = new User(1L, "John", "Doe", "johndoe", "password");
        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(userService.getUserInformationById("1")).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = userRestHandler.getUserInformationById(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(userService, times(1)).getUserInformationById("1");
    }


    @Test
    public void testCreateUserInformation() {
        User user = new User(1L, "John", "Doe", "johndoe", "password");
        when(userService.saveUserInformation(any(User.class))).thenReturn(Mono.just(user));
        when(serverRequest.bodyToMono(User.class)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = userRestHandler.createUserInformation(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(userService, times(1)).saveUserInformation(any(User.class));
    }


    @Test
    public void testUpdateUserInformation() {
        User user = new User(1L, "John", "Doe", "johndoe", "password");
        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(userService.updateUserInformation(eq("1"), any(User.class))).thenReturn(Mono.just(user));
        when(serverRequest.bodyToMono(User.class)).thenReturn(Mono.just(user));

        Mono<ServerResponse> responseMono = userRestHandler.updateUserInformation(serverRequest);
        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().is2xxSuccessful())
                .verifyComplete();

        verify(userService, times(1)).updateUserInformation(eq("1"), any(User.class));
    }


    @Test
    public void testDeleteUserInformation() {
        when(serverRequest.pathVariable("id")).thenReturn("1");
        when(userService.deleteUserInformation("1")).thenReturn(Mono.empty());

        Mono<Void> responseMono = userRestHandler.deleteUserInformation(serverRequest).then();
        StepVerifier.create(responseMono)
                .verifyComplete();

        verify(userService, times(1)).deleteUserInformation("1");
    }




}
