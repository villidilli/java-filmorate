package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.exception.ValidationException.*;


@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {
    @Override
    protected void validate(User obj) throws ValidationException {
        if (obj.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
        if (obj.getName() == null) obj.setName(obj.getLogin());
    }

    @Override
    @GetMapping
    public List<Requestable> getAllObjects() {
        return super.getAllObjects();
    }

    @Override
    @PostMapping
    public ResponseEntity<Requestable> create(@Valid @RequestBody User obj) {
        return super.create(obj);
    }

    @Override
    @PutMapping
    public ResponseEntity<Requestable> update(@Valid @RequestBody User obj) {
        return super.update(obj);
    }

    @Override
    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> exceptionHandler(ValidationException e) {
        return super.exceptionHandler(e);
    }

    @Override
    @ExceptionHandler
    protected ResponseEntity<Map<String, String>> exceptionHandler(NotFoundException e) {
        return super.exceptionHandler(e);
    }
}