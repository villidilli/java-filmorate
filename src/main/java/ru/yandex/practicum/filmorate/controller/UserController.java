package ru.yandex.practicum.filmorate.controller;

import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: [{}]", users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping("/user")
    @ResponseBody
    public User create(@Valid @RequestBody  User user) {
        log.debug(user.toString());
        if (user.getName() == null) user.setName(user.getLogin());
        return user;


    }


}
