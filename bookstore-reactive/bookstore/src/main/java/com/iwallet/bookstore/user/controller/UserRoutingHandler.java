package com.iwallet.bookstore.user.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class UserRoutingHandler {
    private static final String API = "/api/v1/users";
    private static final String ID = "/{id}";
    @Bean
    public RouterFunction<ServerResponse> userInformationRouter(UserRestHandler userRestHandler) {
        return route(GET(API), userRestHandler::getAll)
                .andRoute(POST(API).and(accept(MediaType.APPLICATION_JSON)), userRestHandler::createUserInformation)
                .andRoute(GET(API + ID), userRestHandler::getUserInformationById)
                .andRoute(PUT(API + ID).and(accept(MediaType.APPLICATION_JSON)), userRestHandler::updateUserInformation)
                .andRoute(DELETE(API + ID), userRestHandler::deleteUserInformation);
        //         .andRoute(DELETE(API).and(RequestPredicates.queryParam("name", StringUtils::hasText)), userRestHandler::deleteUserInformationByName);
    }
}
