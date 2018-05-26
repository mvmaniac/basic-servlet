package io.devfactory.core.di.factory.example;

import io.devfactory.core.annotation.Inject;
import io.devfactory.core.annotation.Service;

@Service
public class MyUserService {

    @Inject
    private UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRepository getUserRepository() {
        return this.userRepository;
    }
}
