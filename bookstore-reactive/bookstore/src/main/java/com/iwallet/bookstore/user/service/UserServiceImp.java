package com.iwallet.bookstore.user.service;

import com.iwallet.bookstore.user.dto.User;
import com.iwallet.bookstore.user.repository.IUserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImp implements IUserService {
    private final IUserRepository repository;

    public UserServiceImp(IUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<User> getUserInformationById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<User> getUserInformations() {
        return repository.findAll();
    }

    @Override
    public Mono<User> saveUserInformation(User UserInformationDTO) {
        return repository.save(UserInformationDTO);
    }

    @Override
    public Mono<User> updateUserInformation(String id, User UserMono) {
        return repository.findById(id).flatMap(User -> {
            UserMono.setId(User.getId()); // if there is something else to update do it here.
            return repository.save(UserMono);
        });
    }

    @Override
    public Mono<Void> deleteUserInformation(String id) {
        return repository.deleteById(id);
    }
}
