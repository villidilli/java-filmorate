package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.ID_NOT_IS_BLANK;
import static ru.yandex.practicum.filmorate.exception.ValidationException.LOGIN_NOT_HAVE_SPACE;


@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {
    private static int generatorID = 1;
    private final Map<Integer, User> users = new HashMap<>();

    private void checkLoginForSpace(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
    }

    private void checkNameForBlank(User user) {
        if (user.getName() == null) user.setName(user.getLogin());
    }

    private void checkUserAvailability(User user) throws NotFoundException, ValidationException {
        Integer id = user.getId();
        if (id == null) throw new ValidationException(ID_NOT_IS_BLANK);
        if (users.get(id) == null) throw new NotFoundException(NotFoundException.NOT_FOUND);
    }

    private void loggingChanges(User user) {
        log.debug("Записан объект: {}", user);
        log.debug("Всего пользователей: [{}]", users.size());
        log.debug("ID generator [{}]", generatorID);
    }

    private void loggingException(Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] " + exception.getMessage());
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Всего пользователей: [{}]", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        try {
            checkLoginForSpace(user);
            checkNameForBlank(user);
            user.setId(generatorID++);
            users.put(user.getId(), user);
            loggingChanges(user);
            return ResponseEntity.ok(user);
        } catch (ValidationException e) {
            loggingException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        try {
            checkLoginForSpace(user);
            checkNameForBlank(user);
            checkUserAvailability(user);
            users.put(user.getId(), user);
            loggingChanges(user);
            return ResponseEntity.ok(user);
        } catch (ValidationException e) {
            loggingException(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        } catch (NotFoundException e) {
            loggingException(e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }
}