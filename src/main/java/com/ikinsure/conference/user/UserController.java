package com.ikinsure.conference.user;

import com.ikinsure.conference.user.dto.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("")
    public List<User> getAll() {
        return service.getAll();
    }

    @PostMapping(value = "/register")
    public User create(@RequestBody @Valid UserCommand userCommand) {
        return service.register(userCommand);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody @Valid UserCommand userCommand) {
        return service.updateEmail(id, userCommand);
    }

}
