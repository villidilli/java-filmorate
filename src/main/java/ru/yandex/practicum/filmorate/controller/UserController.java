package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;

import static ru.yandex.practicum.filmorate.exception.ValidationException.*;


@RestController
@Slf4j
@Validated
@RequestMapping("/users")
public class UserController extends Controller<User> {
    @Override
    protected void validate(User obj) throws ValidationException {
        if (obj.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
        if (obj.getName() == null) obj.setName(obj.getLogin());
    }
}