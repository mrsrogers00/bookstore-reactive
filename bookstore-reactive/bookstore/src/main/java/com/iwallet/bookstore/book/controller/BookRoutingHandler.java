package com.iwallet.bookstore.book.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRoutingHandler {

    private static final String API = "/api/v1/books";
    private static final String ID = "/{id}";

    @Bean
    public RouterFunction<ServerResponse> bookInformationRouter(BookRestHandler bookRestHandler) {
        return route(GET(API), bookRestHandler::getAll)
                .andRoute(POST(API).and(accept(MediaType.APPLICATION_JSON)), bookRestHandler::createBookInformation)
                .andRoute(GET(API + ID), bookRestHandler::getBookInformationById)
                .andRoute(PUT(API + ID).and(accept(MediaType.APPLICATION_JSON)), bookRestHandler::updateBookInformation)
                .andRoute(DELETE(API + ID), bookRestHandler::deleteBookInformation);
       //         .andRoute(DELETE(API).and(RequestPredicates.queryParam("name", StringUtils::hasText)), bookRestHandler::deleteBookInformationByName);
    }
}
