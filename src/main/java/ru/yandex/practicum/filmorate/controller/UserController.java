package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidateException;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;

import java.util.List;

import static ru.yandex.practicum.filmorate.util.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidateException.*;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends Controller<User> {
    protected final UserService service;
    protected final UserStorage storage;

    @Autowired
    public UserController(UserService service, UserStorage storage) {
        this.service = service;
        this.storage = storage;
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<User> getAll() {
        return storage.getAll();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user, BindingResult bindResult) {
        return super.create(user, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public User update(@Valid @RequestBody User user, BindingResult bindResult) {
        return super.update(user, bindResult);
    }

    @Override
    protected void customValidate(User obj) throws ValidateException {
        if (obj.getLogin().contains(" ")) throw new ValidateException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (obj.getName() == null) obj.setName(obj.getLogin());
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    @Override
    protected void addInStorage(User obj) {
        storage.add(obj);
    }

    @Override
    protected void updateInStorage(User obj) {
        storage.update(obj);
    }
}