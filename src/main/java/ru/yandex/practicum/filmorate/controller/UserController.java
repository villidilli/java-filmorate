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

import static ru.yandex.practicum.filmorate.exception.NotFoundException.*;
import static ru.yandex.practicum.filmorate.exception.ValidationException.LOGIN_NOT_HAVE_SPACE;


@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    private static int generatorID = 1;

    private void additionalUserValidate(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);

    }

    private void checkLoginForSpace(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
    }

    private void checkNameForBlank(User user) {
        if (user.getName() == null) user.setName(user.getLogin());
    }

    private void checkAvailableByID(int ID) throws NotFoundException {
        if (users.get(ID) == null) throw new NotFoundException(NOT_FOUND);
    }


    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: [{}]", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping("/user")
    @ResponseBody
    public ResponseEntity create(@Valid @RequestBody  User user) {
        try {
            checkLoginForSpace(user);
            checkNameForBlank(user);
            user.setId(generatorID++);
            users.put(user.getId(), user);
            log.debug("Следующее значение ID [{}]", generatorID);
            log.debug("Количество пользователей: [{}]", users.size());
            log.debug("Сохранено: {}", user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/user")
    @ResponseBody
    public ResponseEntity update(@Valid @RequestBody User user, @RequestParam("id") int ID) {
        try {
            log.debug("Запрошено обновление для ID [{}]", ID);
            checkLoginForSpace(user);
            checkNameForBlank(user);
            checkAvailableByID(ID);
            user.setId(ID);
            users.put(ID, user);
            log.debug("Количество пользователей: [{}]", users.size());
            log.debug("Обновлено: {}", user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (ValidationException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}