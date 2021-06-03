package com.ikinsure.conference.user;

import com.ikinsure.conference.user.dto.UserCreateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(value = "/register")
    public User registerUser(@RequestBody @Valid UserCreateCommand userCommand) {
        return service.register(userCommand);
    }

}
