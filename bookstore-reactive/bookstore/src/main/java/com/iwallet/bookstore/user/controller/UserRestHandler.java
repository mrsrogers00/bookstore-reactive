package com.iwallet.bookstore.user.controller;

import com.iwallet.bookstore.book.controller.ValidatorHandler;
import com.iwallet.bookstore.user.dto.User;
import com.iwallet.bookstore.user.service.UserServiceImp;
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
public class UserRestHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRestHandler.class);

    private final ValidatorHandler validator;
    private final UserServiceImp userService;

    public UserRestHandler(ValidatorHandler validator, UserServiceImp userService) {
        this.validator = validator;
        this.userService = userService;
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return userService.getUserInformations().collectList().flatMap(UserInformations -> {
            if (UserInformations.isEmpty()) {
                return ServerResponse.noContent().build();
            }
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(UserInformations));
        });
    }

    public Mono<ServerResponse> getUserInformationById(ServerRequest request) {
        String id = request.pathVariable("id");

        return userService.getUserInformationById(id)
                .flatMap(contact -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(fromValue(contact)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createUserInformation(ServerRequest request) {
        return request.bodyToMono(User.class)
                //     .doOnNext(validator::validate)
                .flatMap(userService::saveUserInformation)
                .doOnSuccess(UserInformationSaved -> LOGGER.info("UserInformation saved with id: {} ", UserInformationSaved.getId()))
                .doOnError(e -> LOGGER.error("Error in saveUserInformation method", e))
                .flatMap(UserInformation -> ServerResponse.created(getToUri(UserInformation)).bodyValue(UserInformation));
    }

    public Mono<ServerResponse> updateUserInformation(ServerRequest request) {
        Mono<User> UserInformationMono = request.bodyToMono(User.class);
        String id = request.pathVariable("id");
        return UserInformationMono
                .flatMap(UserInformation -> userService.updateUserInformation(id, UserInformation))
                .flatMap(UserInformation -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(UserInformation))).switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteUserInformation(ServerRequest request) {
        String id = request.pathVariable("id");
        return userService.deleteUserInformation(id)
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private URI getToUri(User UserInformationSaved) {
        return UriComponentsBuilder.fromPath(("/{id}"))
                .buildAndExpand(UserInformationSaved.getId()).toUri();
    }

}
