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
public class UserController extends ControllerRequestable<User> {
    protected final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getAll() {
        log.debug("/getAll");
        return service.getAll();
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getFriendsById(@PathVariable Integer id) {
        log.debug("/getFriendsById");
        return service.getFriendsById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable Integer id,
                                       @PathVariable Integer otherId) {
        log.debug("/getCommonFriends");
        return service.getCommonFriends(id, otherId);
    }

    @Override
    @GetMapping("/{id}")
    @ResponseStatus(value = HttpStatus.OK)
    public User getById(@PathVariable Integer id) {
        log.debug("/getById");
        return service.getById(id);
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user, BindingResult bindResult) {
        log.debug("/create");
        return service.create(user, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public User update(@Valid @RequestBody User user, BindingResult bindResult) {
        log.debug("/update");
        return service.update(user, bindResult);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addFriend(@PathVariable Integer id,
                          @PathVariable Integer friendId) {
        log.debug("/addFriend");
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteFriend(@PathVariable Integer id,
                             @PathVariable Integer friendId) {
        log.debug("/deleteFriend");
        service.deleteFriend(id, friendId);
    }
}