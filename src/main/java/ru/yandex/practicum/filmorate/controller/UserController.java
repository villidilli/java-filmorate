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
    private int generatorID = 1;
    public static final String LOG_ID_GEN = "ID generator [{}]";
    public static final String LOG_WRITE_OBJECT = "Записан объект: [{}]";
    public static final String LOG_SIZE_USERS = "Всего пользователей: [{}]";
    public static final String LOG_VALIDATION_SUCCESS = "Валидация пройдена успешно";
    private final Map<Integer, User> users = new HashMap<>();

    private void isLoginHasSpace(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
    }

    private void isNameBlank(User user) {
        if (user.getName() == null) user.setName(user.getLogin());
    }

    private void isUserExist(User user) throws NotFoundException, ValidationException {
        Integer id = user.getId();
        if (id == null) throw new ValidationException(ID_NOT_IS_BLANK);
        if (users.get(id) == null) throw new NotFoundException(NotFoundException.NOT_FOUND);
    }

    private void logVariablesCondition() {
        log.debug(LOG_SIZE_USERS, users.size());
        log.debug(LOG_ID_GEN, generatorID);
    }

    private void logException(HttpStatus status, Exception exception) {
        log.debug("[" + exception.getClass().getSimpleName() + "] [" + status.value() + "]" + exception.getMessage());
    }

    @GetMapping
    public List<User> getAllUsers() {
        logVariablesCondition();
        return new ArrayList<>(users.values());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        try {
            isLoginHasSpace(user);
            isNameBlank(user);
            log.debug(LOG_VALIDATION_SUCCESS);
            user.setId(generatorID++);
            users.put(user.getId(), user);
            log.debug(LOG_WRITE_OBJECT, user);
            logVariablesCondition();
            return ResponseEntity.ok(user);
        } catch (ValidationException e) {
            logException(HttpStatus.BAD_REQUEST, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        try {
            isLoginHasSpace(user);
            isNameBlank(user);
            isUserExist(user);
            log.debug(LOG_VALIDATION_SUCCESS);
            users.put(user.getId(), user);
            log.debug(LOG_WRITE_OBJECT, user);
            logVariablesCondition();
            return ResponseEntity.ok(user);
        } catch (ValidationException e) {
            logException(HttpStatus.BAD_REQUEST, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        } catch (NotFoundException e) {
            logException(HttpStatus.NOT_FOUND, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(user);
        }
    }
}