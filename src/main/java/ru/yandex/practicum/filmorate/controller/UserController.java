package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Requestable;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.Message.*;
import static ru.yandex.practicum.filmorate.exception.ValidationException.*;


@RestController
@Slf4j
@Validated
@RequestMapping("/users")
public class UserController extends Controller<User>{
    @Override
    protected void validate(User obj) throws ValidationException {
        if (obj.getLogin().contains(" ")) throw new ValidationException(LOGIN_NOT_HAVE_SPACE);
        if (obj.getName() == null) obj.setName(obj.getLogin());
    }
}