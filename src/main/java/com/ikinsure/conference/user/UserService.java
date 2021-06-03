package com.ikinsure.conference.user;

import com.ikinsure.conference.user.dto.UserCreateCommand;
import com.ikinsure.conference.user.dto.UserUpdateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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

    public User updateEmail(UUID id, UserUpdateCommand userCommand) {
        User user = repository.findById(id).orElseThrow();

        if (!user.getLogin().equals(userCommand.getLogin())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login does not match");
        }

        user.setEmail(userCommand.getEmail());
        return repository.save(user);
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
