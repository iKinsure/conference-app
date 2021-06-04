package com.ikinsure.conference.user;

import com.ikinsure.conference.exception.LocalisedException;
import com.ikinsure.conference.user.dto.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User register(UserCommand userCommand) {


        return repository.save(new User(userCommand.getEmail(), userCommand.getLogin()));
    }

    public User updateEmail(UUID id, UserCommand userCommand) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new LocalisedException("user.not-exists"));

        if (!user.getLogin().equals(userCommand.getLogin())) {
            throw new LocalisedException("login.not-match");
        }

        user.setEmail(userCommand.getEmail());
        return repository.save(user);
    }

    public List<User> getAll() {
        return repository.findAll();
    }
}
