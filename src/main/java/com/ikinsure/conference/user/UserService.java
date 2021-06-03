package com.ikinsure.conference.user;

import com.ikinsure.conference.user.dto.UserCreateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(UserCreateCommand userCommand) {
        return repository.save(new User(
                userCommand.getEmail(),
                userCommand.getLogin()
        ));
    }
}
