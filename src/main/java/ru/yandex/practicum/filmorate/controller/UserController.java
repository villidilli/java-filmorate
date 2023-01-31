package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UnexpectedException;
import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.List;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidationException.*;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends Controller<User> {
    @Override
    protected void customValidate(User obj) throws ValidationException {
        if (obj.getLogin().contains(" ")) throw new ValidationException("[Login] -> " + LOGIN_NOT_HAVE_SPACE);
        if (obj.getName() == null) obj.setName(obj.getLogin());
        log.debug(LOG_CUSTOM_VALID_SUCCESS.message);
    }

    @Override
    @GetMapping
    @ResponseStatus(value = HttpStatus.OK)
    public List<Requestable> getAllObjects() {
        return super.getAllObjects();
    }

    @Override
    @PostMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Requestable create(@Valid @RequestBody User obj, BindingResult bindResult) {
        return super.create(obj, bindResult);
    }

    @Override
    @PutMapping
    @ResponseStatus(value = HttpStatus.OK)
    public Requestable update(@Valid @RequestBody User obj, BindingResult bindResult) {
        return super.update(obj, bindResult);
    }
}