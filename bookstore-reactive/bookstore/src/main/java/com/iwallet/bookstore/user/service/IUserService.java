package com.iwallet.bookstore.user.service;

import com.iwallet.bookstore.user.dto.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserService {
    Mono<User> getUserInformationById(String id);

    Flux<User> getUserInformations();

    Mono<User> saveUserInformation(User UserInformationDTO);

    Mono<User> updateUserInformation(String id, User UserInformationDTO);

    Mono<Void> deleteUserInformation(String id);
}
