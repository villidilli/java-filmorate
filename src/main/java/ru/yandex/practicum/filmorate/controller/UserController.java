package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController implements Controller<User> {
    protected final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getAll() {
        return service.getAll();
    }


    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user, BindingResult bindResult) {
        return service.create(user, bindResult);
    }

    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public User update(@Valid @RequestBody User user, BindingResult bindResult) {
        return service.update(user, bindResult);
    }
}